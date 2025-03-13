package edu.sabanciuniv.projectbackend.services;

import edu.sabanciuniv.projectbackend.models.Discount;
import edu.sabanciuniv.projectbackend.repositories.DiscountRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscountService {

    private final DiscountRepository discountRepository;

    public DiscountService(DiscountRepository discountRepository) {
        this.discountRepository = discountRepository;
    }

    public List<Discount> getAllDiscounts() {
        return discountRepository.findAll();
    }

    public Discount getDiscountById(String discountId) {
        return discountRepository.findById(discountId).orElse(null);
    }

    public Discount saveDiscount(Discount discount) {
        return discountRepository.save(discount);
    }

    public void deleteDiscount(String discountId) {
        discountRepository.deleteById(discountId);
    }
}
