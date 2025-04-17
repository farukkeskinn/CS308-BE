package edu.sabanciuniv.projectbackend.models;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @Column(name = "order_item_id", columnDefinition = "CHAR(36)")
    private String orderItemId;

    @Column(name = "price_at_purchase", nullable = false)
    private Double priceAtPurchase;

    @Column(nullable = false)
    private Integer quantity;

    // Relationship to Order
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    @JsonIgnore
    private Order order;

    // Relationship to Product
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // Constructors, Getters, Setters
    public String getOrderItemId() {
        return orderItemId;
    }

    public Order getOrder() {
        return order;
    }

    public Product getProduct() {
        return product;
    }

    public Double getPriceAtPurchase() {
        return priceAtPurchase;
    }

    public Integer getQuantity() {
        return quantity;
    }


    public void setOrder(Order order) {
        this.order = order;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setPriceAtPurchase(Double priceAtPurchase) {
        this.priceAtPurchase = priceAtPurchase;
    }

    public void setOrderItemId(String orderItemId) { this.orderItemId = orderItemId; }

}
