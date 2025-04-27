package edu.sabanciuniv.projectbackend.repositories;

import edu.sabanciuniv.projectbackend.models.ShoppingCartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingCartItemRepository extends JpaRepository<ShoppingCartItem, String> {
    long countByShoppingCart_CartId(String cartId);
    @Query("SELECT i FROM ShoppingCartItem i " +
            "WHERE i.shoppingCart.cartId = :cartId " +
            "  AND i.product.productId = :productId")
    ShoppingCartItem findByCartIdAndProductId(@Param("cartId") String cartId,
                                              @Param("productId") String productId);
}