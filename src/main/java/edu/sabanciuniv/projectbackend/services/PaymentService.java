package edu.sabanciuniv.projectbackend.services;

import edu.sabanciuniv.projectbackend.dto.OrderSummaryDTO;
import edu.sabanciuniv.projectbackend.models.Order;
import edu.sabanciuniv.projectbackend.models.Payment;
import edu.sabanciuniv.projectbackend.models.Address;
import edu.sabanciuniv.projectbackend.repositories.PaymentRepository;
import org.springframework.stereotype.Service;
import edu.sabanciuniv.projectbackend.dto.InvoiceResponse;

import edu.sabanciuniv.projectbackend.repositories.ProductRepository;

import edu.sabanciuniv.projectbackend.dto.PaymentRequest;
import edu.sabanciuniv.projectbackend.models.ShoppingCart;
import edu.sabanciuniv.projectbackend.models.ShoppingCartItem;
import edu.sabanciuniv.projectbackend.utils.EncryptionUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PaymentService {
    private final ShoppingCartService cartService;
    private final OrderService orderService;
    private final EmailService emailService;
    private final InvoiceGeneratorService invoiceGeneratorService;
    private final PaymentRepository paymentRepository;
    private final ShoppingCartService shoppingCartService;
    private final ProductRepository productRepository;
    private final EncryptionUtil encryptionUtil;

    private final AddressService addressService;

    public PaymentService(PaymentRepository paymentRepository,
                          ShoppingCartService cartService,
                          OrderService orderService,
                          EmailService emailService,
                          InvoiceGeneratorService invoiceGeneratorService, ShoppingCartService shoppingCartService, ProductRepository productRepository, AddressService addressService,
                          EncryptionUtil encryptionUtil) {
        this.paymentRepository = paymentRepository;
        this.cartService = cartService;
        this.orderService = orderService;
        this.emailService = emailService;
        this.invoiceGeneratorService = invoiceGeneratorService;
        this.shoppingCartService = shoppingCartService;
        this.productRepository = productRepository;
        this.addressService = addressService;
        this.encryptionUtil = encryptionUtil;
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Payment getPaymentById(String paymentId) {
        return paymentRepository.findById(paymentId).orElse(null);
    }

    public Payment savePayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    public void deletePayment(String paymentId) {
        paymentRepository.deleteById(paymentId);
    }

    public InvoiceResponse processCheckout(PaymentRequest request, String username) {

        // 1️⃣ Kullanıcının sepetini al
        ShoppingCart cart = cartService.getCartByUsername(username);
        List<ShoppingCartItem> items = cart.getShoppingCartItems();

        if (items.isEmpty()) {
            throw new RuntimeException("Cart is empty.");
        }

        // 2️⃣ Toplam fiyat hesapla
        double totalAmount = items.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();

        // 3️⃣ Siparişi oluştur
        Order order = orderService.createOrderFromCart(cart);

        // 3.1️⃣ Ürün stoklarını güncelle
        for (ShoppingCartItem item : items) {
            var product = item.getProduct();
            int boughtQty = item.getQuantity();

            product.setItemSold(product.getItemSold() + boughtQty);// satış sayısını artır
            product.setQuantity(product.getQuantity() - boughtQty);

            // Optional: stok 0'ın altına düşmesin
            if (product.getQuantity() < 0) {
                throw new RuntimeException("Product stock cannot go negative: " + product.getName());
            }

            // ürünü güncelle
            productRepository.save(product);
        }

        // 4️⃣ Mock ödeme onayı (pop-up frontend'de)
        System.out.println("Mock payment approved for card: " + request.getCardNumber());
        order.setPaymentStatus("DONE");
        orderService.saveOrder(order);

        // 🔒 5️⃣ Şifreli kart bilgisi oluştur
        String rawInfo = "Card: " + request.getCardNumber() +
                ", Name: " + request.getCardHolderName() +
                ", Exp: " + request.getExpiryDate() +
                ", CVV: " + request.getCvv();
        String encryptedInfo = encryptionUtil.encryptString(rawInfo);

        // 💾 6️⃣ Payment nesnesi oluştur ve kaydet
        Payment payment = new Payment();
        payment.setPaymentId(UUID.randomUUID().toString());
        payment.setEncryptedCreditCardInfo(encryptedInfo);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setAmount(totalAmount);
        payment.setOrder(order);
        payment.setPaymentStatus("DONE");
        paymentRepository.save(payment);

        // 📄 7️⃣ Fatura PDF oluştur
        String pdfPath = invoiceGeneratorService.generateInvoicePdf(order, request);
        String encUrl;
        if (pdfPath.startsWith("http")){
            encUrl = encryptionUtil.encryptString(pdfPath);
            order.setInvoiceLink(encUrl);
        }
        else{
            order.setInvoiceLink(pdfPath);
        }
        orderService.saveOrder(order);

        // 📧 8️⃣ E-posta gönder
        emailService.sendInvoiceEmail(order.getCustomer().getEmail(), pdfPath);

        shoppingCartService.clearCart(username);


        // 🔟 Adresi adresses tablosuna ekle
        Address address = new Address();
        address.setAddressId(UUID.randomUUID().toString());

        String addressLine = request.getAddress() + ", " +
                request.getCity() + ", " +
                request.getCountry() + ", " +
                request.getZipCode();
        String addressName = request.getAddressName();

        address.setAddressLine(addressLine);
        address.setAddressName(addressName);
        address.setCustomer(order.getCustomer()); // veya order.getCustomer().getCustomerId() eğer UUID string

        addressService.saveAddress(address);

        // ─── STEP 3: LINK SHIPPING ADDRESS TO ORDER ───
        // (adds order.address_id → your new Address)
        order.setShippingAddress(address);
        orderService.saveOrder(order);


        // 9️⃣ Yanıtı hazırla
        InvoiceResponse response = new InvoiceResponse();
        response.setOrderId(order.getOrderId());
        response.setCustomerName(order.getCustomer().getFirstName() + " " + order.getCustomer().getLastName());
        response.setEmail(order.getCustomer().getEmail());
        response.setTotalAmount(totalAmount);
        response.setProductNames(
                items.stream().map(item -> item.getProduct().getName()).toList()
        );
        response.setInvoicePdfUrl(encryptionUtil.decryptString(pdfPath));

        return response;
    }
}