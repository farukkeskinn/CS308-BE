package edu.sabanciuniv.projectbackend.controllers;

import edu.sabanciuniv.projectbackend.models.SalesManager;
import edu.sabanciuniv.projectbackend.services.SalesManagerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales-managers")
@CrossOrigin(origins = "http://localhost:3000")
public class SalesManagerController {

    private final SalesManagerService salesManagerService;

    public SalesManagerController(SalesManagerService salesManagerService) {
        this.salesManagerService = salesManagerService;
    }

    @GetMapping
    public List<SalesManager> getAllSalesManagers() {
        return salesManagerService.getAllSalesManagers();
    }

    @GetMapping("/{id}")
    public SalesManager getSalesManagerById(@PathVariable("id") String smId) {
        return salesManagerService.getSalesManager(smId);
    }

    @PostMapping
    public SalesManager createSalesManager(@RequestBody SalesManager salesManager) {
        return salesManagerService.saveSalesManager(salesManager);
    }

    @DeleteMapping("/{id}")
    public void deleteSalesManager(@PathVariable("id") String smId) {
        salesManagerService.deleteSalesManager(smId);
    }
}
