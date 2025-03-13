package edu.sabanciuniv.projectbackend.models;
import jakarta.persistence.*;

@Entity
@Table(name = "productmanagers")
public class ProductManager {

    @Id
    @Column(name = "pm_id", columnDefinition = "CHAR(36)")
    private String pmId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    // Constructors, Getters, Setters
    public String getPmId() {
        return pmId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setPmId(String pmId) {
        this.pmId = pmId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
