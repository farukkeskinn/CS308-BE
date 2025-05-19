package edu.sabanciuniv.projectbackend.services;

import edu.sabanciuniv.projectbackend.models.Address;
import edu.sabanciuniv.projectbackend.models.Delivery;
import edu.sabanciuniv.projectbackend.models.Order;
import edu.sabanciuniv.projectbackend.repositories.DeliveryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Service
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final OrderService orderService;       // to load the Order
    private final AddressService addressService;

    public DeliveryService(DeliveryRepository dr,
                           OrderService os,
                           AddressService as) {
        this.deliveryRepository    = dr;
        this.orderService    = os;
        this.addressService  = as;
    }

    @Transactional
    public Delivery createForOrder(String orderId, String addressId) {
        // 1) fetch the order
        Order order = orderService.getOrderById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Order not found: " + orderId);
        }

        // 2) only fetch an address if we got a non-null ID
        Address addr = null;
        if (addressId != null) {
            addr = addressService.getAddressById(addressId);
            if (addr == null) {
                throw new IllegalArgumentException("Address not found: " + addressId);
            }
        }

        // 3) build & save the Delivery
        Delivery d = new Delivery();
        d.setDeliveryId(UUID.randomUUID().toString());
        d.setOrder(order);
        d.setAddress(addr);                       // addr may be null now
        d.setDeliveryStatus("IN_TRANSIT");
        return deliveryRepository.save(d);
    }

    /** once delivered, update that Delivery row too */
    @Transactional
    public Delivery markDelivered(String deliveryId) {
        Delivery d = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new IllegalArgumentException("No such delivery"));
        d.setDeliveryStatus("DELIVERED");
        return deliveryRepository.save(d);
    }

    @Transactional
    public Delivery markDeliveredForOrder(String orderId) {
        Delivery d = deliveryRepository
                .findByOrder_OrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("No delivery for order " + orderId));
        d.setDeliveryStatus("DELIVERED");
        return deliveryRepository.save(d);
    }

    public List<Delivery> getAllDeliveries() {
        return deliveryRepository.findAll();
    }

    public Delivery getDeliveryById(String deliveryId) {
        return deliveryRepository.findById(deliveryId).orElse(null);
    }

    @Transactional
    public Delivery saveDelivery(Delivery delivery) {
        return deliveryRepository.save(delivery);
    }

    @Transactional
    public void deleteDelivery(String deliveryId) {
        deliveryRepository.deleteById(deliveryId);
    }
}
