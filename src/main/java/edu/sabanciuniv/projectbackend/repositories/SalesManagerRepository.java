package edu.sabanciuniv.projectbackend.repositories;

import edu.sabanciuniv.projectbackend.models.SalesManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesManagerRepository extends JpaRepository<SalesManager, String> {
    // Additional queries if needed
}
