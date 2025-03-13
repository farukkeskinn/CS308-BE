package edu.sabanciuniv.projectbackend.models;
import jakarta.persistence.*;

@Entity
@Table(name = "salesmanagers")
public class SalesManager {

    @Id
    @Column(name = "sm_id", columnDefinition = "CHAR(36)")
    private String smId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    // Constructors, Getters, Setters
    public String getSmId() {
        return smId;
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
}
