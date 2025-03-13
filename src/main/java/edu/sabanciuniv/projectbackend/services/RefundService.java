package edu.sabanciuniv.projectbackend.services;

import edu.sabanciuniv.projectbackend.models.Refund;
import edu.sabanciuniv.projectbackend.repositories.RefundRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RefundService {

    private final RefundRepository refundRepository;

    public RefundService(RefundRepository refundRepository) {
        this.refundRepository = refundRepository;
    }

    public List<Refund> getAllRefunds() {
        return refundRepository.findAll();
    }

    public Refund getRefundById(String refundId) {
        return refundRepository.findById(refundId).orElse(null);
    }

    public Refund saveRefund(Refund refund) {
        return refundRepository.save(refund);
    }

    public void deleteRefund(String refundId) {
        refundRepository.deleteById(refundId);
    }
}
