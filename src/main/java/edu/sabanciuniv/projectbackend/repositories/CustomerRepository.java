package edu.sabanciuniv.projectbackend.repositories;

import edu.sabanciuniv.projectbackend.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    Customer findByEmail(String email);
    Optional<Customer> findOptionalByEmail(String email);

}
