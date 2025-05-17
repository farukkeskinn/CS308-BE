package edu.sabanciuniv.projectbackend.repositories;

import org.springframework.stereotype.Repository;
import edu.sabanciuniv.projectbackend.models.Customer;
import edu.sabanciuniv.projectbackend.models.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, String> {

    @Query("SELECT s FROM ShoppingCart s WHERE s.customer.customerId = :customerId")
    ShoppingCart findByCustomerId(@Param("customerId") String customerId);

    ShoppingCart findByCustomerEmail(String email);
    ShoppingCart findByCustomer(Customer customer);

}

