package org.example.repositories;

import org.example.entities.PurchaseReceiptItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseReceiptItemRepository extends JpaRepository<PurchaseReceiptItem, Long> {
    PurchaseReceiptItem findById(long id);
}
