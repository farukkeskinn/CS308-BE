package edu.sabanciuniv.projectbackend.repositories;

import edu.sabanciuniv.projectbackend.models.ShoppingCartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingCartItemRepository extends JpaRepository<ShoppingCartItem, String> {
    // Additional queries if needed
}
