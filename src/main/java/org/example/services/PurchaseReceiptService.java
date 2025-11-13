package org.example.services;

import org.example.entities.*;
import org.example.repositories.PurchaseReceiptRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Handles the business logic of converting a ShoppingCart into a
 * persistent PurchaseReceipt.
 */
@Service
public class PurchaseReceiptService {

    private final PurchaseReceiptRepository purchaseReceiptRepository;

    public PurchaseReceiptService(PurchaseReceiptRepository purchaseReceiptRepository) {
        this.purchaseReceiptRepository = purchaseReceiptRepository;
    }

    /**
     * Builds a new, !unpersisted! PurchaseReceipt object from a user's cart.
     * This method does not save to the database; it only prepares the object.
     * @param cart The ShoppingCart associated with the user.
     * @return A new, populated, but unsaved PurchaseReceipt object.
     * @throws IllegalStateException if the cart is empty.
     */
    public PurchaseReceipt buildReceiptFromCart(ShoppingCart cart) {
        if (cart == null || cart.getItems().isEmpty()) {
            throw new IllegalStateException("Cannot build receipt... empty cart provided.");
        }
        User user = cart.getUser();

        // Initialize receipt
        PurchaseReceipt receipt = new PurchaseReceipt();
        receipt.setUser(user);
        receipt.setOrderDateTime(LocalDateTime.now());

        // Load into from cart items into receipt items
        List<PurchaseReceiptItem> receiptItems = new ArrayList<PurchaseReceiptItem>();
        List<ShoppingCartItem> shoppingCartItems = cart.getItems();
        double receiptTotal = 0;
        for(ShoppingCartItem scItem : shoppingCartItems) {
            PurchaseReceiptItem prItem = new PurchaseReceiptItem();
            prItem.setPurchaseReceipt(receipt);
            prItem.setBook(scItem.getBook());
            prItem.setQuantity(scItem.getQuantity());
            prItem.setPricePerBook(scItem.getBook().getPrice());

            receiptItems.add(prItem);

            receiptTotal += prItem.getPricePerBook() * prItem.getQuantity();
        }

        // Finish adding purchase receipt information
        receipt.setItems(receiptItems);
        receipt.setTotalCost(receiptTotal);

        // NOT PERSISTED (same with items)
        return receipt;
    }

    // METHODS FOR VIEWING RECEIPTS
    @Transactional(readOnly = true)
    public List<PurchaseReceipt> getReceiptsForUser(User user) {
        return purchaseReceiptRepository.findByUserOrderByOrderDateTimeDesc(user);
    }

    @Transactional(readOnly = true)
    public Optional<PurchaseReceipt> getReceiptForUser(Long receiptId, User user) {
        Optional<PurchaseReceipt> receiptOpt = purchaseReceiptRepository.findById(receiptId);

        if (receiptOpt.isEmpty()) {
            return Optional.empty();
        }
        PurchaseReceipt receipt = receiptOpt.get();
        // If no user attached or provided user does not match the fetched receipt
        if (receipt.getUser() == null || receipt.getUser().getId() != user.getId()) {
            return Optional.empty();
        }

        return receiptOpt;
    }
}