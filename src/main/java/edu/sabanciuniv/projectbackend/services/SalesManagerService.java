package edu.sabanciuniv.projectbackend.services;

import edu.sabanciuniv.projectbackend.models.SalesManager;
import edu.sabanciuniv.projectbackend.repositories.SalesManagerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalesManagerService {

    private final SalesManagerRepository salesManagerRepository;

    public SalesManagerService(SalesManagerRepository salesManagerRepository) {
        this.salesManagerRepository = salesManagerRepository;
    }

    public List<SalesManager> getAllSalesManagers() {
        return salesManagerRepository.findAll();
    }

    public SalesManager getSalesManager(String smId) {
        return salesManagerRepository.findById(smId).orElse(null);
    }

    public SalesManager saveSalesManager(SalesManager salesManager) {
        return salesManagerRepository.save(salesManager);
    }

    public void deleteSalesManager(String smId) {
        salesManagerRepository.deleteById(smId);
    }
}
