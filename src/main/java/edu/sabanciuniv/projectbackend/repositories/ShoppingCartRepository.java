package edu.sabanciuniv.projectbackend.repositories;

import edu.sabanciuniv.projectbackend.models.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, String> {
    // Additional queries if needed
}
