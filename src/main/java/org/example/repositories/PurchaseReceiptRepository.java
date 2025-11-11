package org.example.repositories;

import org.example.entities.PurchaseReceipt;
import org.example.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PurchaseReceiptRepository extends JpaRepository<PurchaseReceipt, Long> {
    Optional<PurchaseReceipt> findById(long id);
    PurchaseReceipt findByOrderDateTime(LocalDateTime orderDateTime);
    List<PurchaseReceipt> findByUserOrderByOrderDateTimeDesc(User user);
}
