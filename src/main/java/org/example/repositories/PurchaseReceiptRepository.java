package org.example.repositories;

import org.example.entities.PurchaseReceipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;

@Repository
public interface PurchaseReceiptRepository extends JpaRepository<PurchaseReceipt, Long> {
    PurchaseReceipt findById(long id);
    PurchaseReceipt findByOrderDateTime(LocalDateTime orderDateTime);
}
