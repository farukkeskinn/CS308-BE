package edu.sabanciuniv.projectbackend.models;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "addresses")
public class Address {

    @Id
    @Column(name = "address_id", columnDefinition = "CHAR(36)")
    private String addressId;

    @Column(name = "address_line", nullable = false, columnDefinition = "TEXT")
    private String addressLine;


    @Column(name = "address_name")
    private String addressName;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonIgnore
    private Customer customer;

    // Constructors, Getters, Setters
    public String getAddressId() {
        return addressId;
    }

    public String getAddressLine() {
        return addressLine;
    }

    public String getAddressName() {
        return addressName;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setAddressId(String addressId) { this.addressId = addressId; }

    public void setAddressLine(String addressLine) { this.addressLine = addressLine; }

    public void setAddressName(String addressName) { this.addressName = addressName; }

    public void setCustomer(Customer customer) { this.customer = customer; }
}
