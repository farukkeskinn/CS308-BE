package edu.sabanciuniv.projectbackend.repositories;

import edu.sabanciuniv.projectbackend.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    // Additional custom queries if needed
    @Query("SELECT p FROM Product p " +
            "WHERE p.category.categoryId = :categoryId " +
            "   OR (p.category.parentCategory IS NOT NULL " +
            "       AND p.category.parentCategory.categoryId = :categoryId)")
    Page<Product> findByCategory(@Param("categoryId") Integer categoryId, Pageable pageable);


}
