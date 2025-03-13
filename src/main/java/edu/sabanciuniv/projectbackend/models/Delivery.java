package edu.sabanciuniv.projectbackend.models;
import jakarta.persistence.*;

@Entity
@Table(name = "deliveries")
public class Delivery {

    @Id
    @Column(name = "delivery_id", columnDefinition = "CHAR(36)")
    private String deliveryId;

    @Column(name = "delivery_status", nullable = false)
    private String deliveryStatus;

    // Relationship to Order
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // Relationship to Address
    @ManyToOne
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    // Constructors, Getters, Setters
    public String getDeliveryId() {
        return deliveryId;
    }

    public Order getOrder() {
        return order;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public Address getAddress() {
        return address;
    }
}
