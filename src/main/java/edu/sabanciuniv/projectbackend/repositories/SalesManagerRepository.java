package edu.sabanciuniv.projectbackend.repositories;

import edu.sabanciuniv.projectbackend.models.Order;
import edu.sabanciuniv.projectbackend.models.SalesManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalesManagerRepository extends JpaRepository<SalesManager, String> {
    SalesManager findByEmail(String email);

    @Query("SELECT o FROM Order o")
    List<Order> findAllOrders();
}
