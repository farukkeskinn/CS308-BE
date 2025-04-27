package edu.sabanciuniv.projectbackend.repositories;

import edu.sabanciuniv.projectbackend.models.ProductManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductManagerRepository extends JpaRepository<ProductManager, String> {
    ProductManager findByEmail(String email);
}
