package edu.sabanciuniv.projectbackend.services;

import edu.sabanciuniv.projectbackend.dto.AddItemRequest;
import edu.sabanciuniv.projectbackend.models.*;
import edu.sabanciuniv.projectbackend.repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class WishlistService {

    private final WishlistRepository     wishlistRepository;
    private final WishlistItemRepository wishlistItemRepository;
    private final CustomerRepository     customerRepository;
    private final ProductRepository      productRepository;
    private final CustomerService customerService;
    private final ProductService productService;

    public WishlistService(WishlistRepository     wishlistRepository,
                           WishlistItemRepository wishlistItemRepository,
                           CustomerRepository     customerRepository,
                           ProductRepository      productRepository,
                           CustomerService        customerService,
                           ProductService         productService) {

        this.wishlistRepository     = wishlistRepository;
        this.wishlistItemRepository = wishlistItemRepository;
        this.customerRepository     = customerRepository;
        this.productRepository      = productRepository;
        this.customerService        = customerService;
        this.productService         = productService;
    }


    public Customer getCustomerById(String customerId) {
        return customerRepository.findById(customerId).orElse(null);
    }

    /** Müşteriye ait wishlist’i getirir; yoksa null. */
    public Wishlist getWishlistByCustomer(Customer customer) {
        return wishlistRepository
                .findByCustomerCustomerId(customer.getCustomerId())
                .orElse(null);
    }


    /* ---------------------------------------------------------------
       ÜRÜN EKLE   (email + productId)
       --------------------------------------------------------------- */
    /**
     * Ürünü kullanıcının wishlist'ine ekler.
     *
     * @param req  { customerId, productId }
     * @return     güncel Wishlist  (ürün zaten listede ise olduğu hâli)
     *             null            (ürün stokta yoksa veya ürün bulunamazsa)
     */
    @Transactional
    public Wishlist addProductToWishlist(AddItemRequest req) {

        /* --------- 0. Müşteri + Ürün kontrolü ------------------- */
        Customer customer = customerRepository.findById(req.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Product product = productRepository.findById(req.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        /* --------- 1. Wishlist getir / oluştur ------------------ */
        Wishlist wishlist = wishlistRepository
                .findByCustomerCustomerId(customer.getCustomerId())
                .orElseGet(() -> {
                    Wishlist wl = new Wishlist();
                    wl.setWishlistId(UUID.randomUUID().toString());
                    wl.setCustomer(customer);
                    wl.setWishlistStatus("ACTIVE");
                    return wishlistRepository.save(wl);
                });

        /* --------- 2. Zaten ekli mi? ---------------------------- */
        boolean exists = wishlistItemRepository
                .existsByWishlistWishlistIdAndProductProductId(
                        wishlist.getWishlistId(), product.getProductId());

        if (exists) {
            return wishlist;         // değişiklik yok, mevcut hâlini döner
        }

        String wishlistId = wishlist.getWishlistId();
        boolean isEmpty = wishlistItemRepository
                .findAll()
                .stream()
                .noneMatch(i -> i.getWishlist().getWishlistId().equals(wishlistId));


        /* --------- 3. Yeni WishlistItem ------------------------- */
        WishlistItem item = new WishlistItem();
        item.setWishlistItemId(UUID.randomUUID().toString());
        item.setWishlist(wishlist);
        item.setProduct(product);

        wishlist.getWishlistItems().add(item);   // ilişkiyi tutarlı kıl
        wishlist.setWishlistStatus("ACTIVE");
        return wishlist;                         // @Transactional sayesinde otomatik kaydedilir
    }



    /**
     * Bir ürün‐wishlist ilişkisinin (WishlistItem) tamamen silinmesi.
     * <p>
     * • itemId   →  wishlist_items tablosundaki PK
     * • Eğer silme sonrası wishlist boş kaldıysa
     *     status “EMPTY”, aksi hâlde “ACTIVE” olarak güncellenir.
     */
    @Transactional
    public void removeProductFromWishlist(String itemId) {

        /* --- 1) WishlistItem’i bul ------------------------------------------------ */
        WishlistItem item = wishlistItemRepository.findById(itemId)
                .orElse(null);
        if (item == null) return;   // zaten yoksa sessizce çık

        String wishlistId = item.getWishlist().getWishlistId();

        /* --- 2) İlişkiyi sil ------------------------------------------------------ */
        wishlistItemRepository.deleteById(itemId);

        /* --- 3) Wishlist’in son durumunu güncelle -------------------------------- */
        Wishlist wl = wishlistRepository.findById(wishlistId).orElse(null);
        if (wl == null) return;     // teorik olarak mümkün değil ama koruyucu

        boolean isEmpty = wishlistItemRepository
                .findAll()
                .stream()
                .noneMatch(i -> i.getWishlist().getWishlistId().equals(wishlistId));

        wl.setWishlistStatus(isEmpty ? "EMPTY" : "ACTIVE");
        wishlistRepository.save(wl);
    }
























    /* ---------------------------------------------------------------
       LİSTEDE Mİ?  (email + productId)
       --------------------------------------------------------------- */
    public boolean isInWishlist(String email, String productId) {

        return wishlistRepository.findByCustomerEmail(email)
                .map(Wishlist::getWishlistId)
                .map(id -> wishlistItemRepository
                        .existsByWishlistWishlistIdAndProductProductId(id, productId))
                .orElse(false);
    }

    /* ---------------------------------------------------------------
       ADMIN işlemleri (değişmedi)
       --------------------------------------------------------------- */
    public List<Wishlist> getAllWishlists()        { return wishlistRepository.findAll(); }
    public Wishlist getWishlistById(String id)     { return wishlistRepository.findById(id).orElse(null); }
    public Wishlist saveWishlist(Wishlist w)       { return wishlistRepository.save(w); }
    public void     deleteWishlist(String id)      { wishlistRepository.deleteById(id); }
}
