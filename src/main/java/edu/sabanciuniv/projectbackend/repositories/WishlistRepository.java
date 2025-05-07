package edu.sabanciuniv.projectbackend.repositories;

import edu.sabanciuniv.projectbackend.models.Customer;
import edu.sabanciuniv.projectbackend.models.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, String> {
    // müşteri e-postası üzerinden wishlist getir
    Optional<Wishlist> findByCustomerEmail(String email);

    // ID üzerinden isteyen eski kodlar için
    Optional<Wishlist> findByCustomerCustomerId(String customerId);
}