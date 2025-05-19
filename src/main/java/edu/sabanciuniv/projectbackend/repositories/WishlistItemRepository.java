package edu.sabanciuniv.projectbackend.repositories;

import edu.sabanciuniv.projectbackend.models.Customer;
import edu.sabanciuniv.projectbackend.models.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishlistItemRepository extends JpaRepository<WishlistItem, String> {
    boolean existsByWishlistWishlistIdAndProductProductId(String wishlistId, String productId);
    void deleteByWishlistWishlistIdAndProductProductId(String wishlistId, String productId);

    @Query("""
       select distinct wi.wishlist.customer
       from WishlistItem wi
       where wi.product.productId = :pid
    """)
    List<Customer> findCustomersByProduct(String pid);
}
