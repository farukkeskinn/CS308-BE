package edu.sabanciuniv.projectbackend.controllers;

import edu.sabanciuniv.projectbackend.models.Refund;
import edu.sabanciuniv.projectbackend.services.RefundService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/refunds")
@CrossOrigin(origins = "http://localhost:3000")
public class RefundController {

    private final RefundService refundService;

    public RefundController(RefundService refundService) {
        this.refundService = refundService;
    }

    @GetMapping
    public List<Refund> getAllRefunds() {
        return refundService.getAllRefunds();
    }

    @GetMapping("/{id}")
    public Refund getRefundById(@PathVariable("id") String refundId) {
        return refundService.getRefundById(refundId);
    }

    @PostMapping
    public Refund createRefund(@RequestBody Refund refund) {
        return refundService.saveRefund(refund);
    }

    @DeleteMapping("/{id}")
    public void deleteRefund(@PathVariable("id") String refundId) {
        refundService.deleteRefund(refundId);
    }
}
