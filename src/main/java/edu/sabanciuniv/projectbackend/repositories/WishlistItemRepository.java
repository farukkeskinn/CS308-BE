package edu.sabanciuniv.projectbackend.repositories;

import edu.sabanciuniv.projectbackend.models.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistItemRepository extends JpaRepository<WishlistItem, String> {
    // Additional queries if needed
}
