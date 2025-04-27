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

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    public String getDeliveryId() { return deliveryId; }
    public String getDeliveryStatus() { return deliveryStatus; }
    public Order getOrder() { return order; }
    public Address getAddress() { return address; }

    public void setDeliveryId(String deliveryId) {
        this.deliveryId = deliveryId;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
