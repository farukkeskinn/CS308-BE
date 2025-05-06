package edu.sabanciuniv.projectbackend.repositories;

import edu.sabanciuniv.projectbackend.models.Refund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RefundRepository extends JpaRepository<Refund, String> {
    @Query("SELECT r FROM Refund r WHERE r.refundStatus = 'PENDING'")
    List<Refund> findPendingRefunds();

    @Query("SELECT r FROM Refund r WHERE r.requestDate >= ?1 AND r.requestDate <= ?2 AND r.refundStatus IN ('APPROVED', 'REJECTED')")
    List<Refund> findProcessedRefundsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
}
