package edu.sabanciuniv.projectbackend.controllers;

import edu.sabanciuniv.projectbackend.models.Discount;
import edu.sabanciuniv.projectbackend.services.DiscountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/discounts")
@CrossOrigin(origins = "http://localhost:3000")
public class DiscountController {

    private final DiscountService discountService;

    public DiscountController(DiscountService discountService) {
        this.discountService = discountService;
    }

    @GetMapping
    public List<Discount> getAllDiscounts() {
        return discountService.getAllDiscounts();
    }

    @GetMapping("/{id}")
    public Discount getDiscountById(@PathVariable("id") String discountId) {
        return discountService.getDiscountById(discountId);
    }

    @PostMapping
    public Discount createDiscount(@RequestBody Discount discount) {
        return discountService.saveDiscount(discount);
    }

    @DeleteMapping("/{id}")
    public void deleteDiscount(@PathVariable("id") String discountId) {
        discountService.deleteDiscount(discountId);
    }
}
