package org.example.services;

import org.example.entities.*;
import org.example.repositories.BookRepository;
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
    private final BookService bookService;

    public PurchaseReceiptService(PurchaseReceiptRepository purchaseReceiptRepository, BookService bookService) {
        this.purchaseReceiptRepository = purchaseReceiptRepository;
        this.bookService = bookService;
    }

    /**
     * Builds a new, !unpersisted! PurchaseReceipt object from a user's cart.
     * This method does not save to the database; it only prepares the object.
     * @param cart The ShoppingCart associated with the user.
     * @return A new, populated, but unsaved PurchaseReceipt object.
     * @throws IllegalStateException if the cart is empty.
     */
    /**
    public PurchaseReceipt buildReceiptFromCart(ShoppingCart cart) {
        if (cart == null || cart.getItems().isEmpty()) {
            throw new IllegalStateException("Cannot build receipt... empty cart provided.");
        }

        // Initialize receipt
        PurchaseReceipt receipt = new PurchaseReceipt();
        receipt.setOrderDateTime(LocalDateTime.now());

        // Load into from cart items into receipt items
        List<PurchaseReceiptItem> receiptItems = new ArrayList<>();
        double total = 0;

        for (ShoppingCartItem scItem : cart.getItems()) {
            PurchaseReceiptItem prItem =
                    new PurchaseReceiptItem(receipt, scItem.getBook(), scItem.getQuantity(), scItem.getBook().getPrice());

            receiptItems.add(prItem);
            total += prItem.getPricePerBook() * prItem.getQuantity();
        }

        // Finish adding purchase receipt information
        receipt.setItems(receiptItems);
        receipt.setTotalCost(total);

        // NOT PERSISTED (same with items)
        return receipt;
    }
     */

    @Transactional
    public PurchaseReceipt buildAndSaveReceiptFromCart(ShoppingCart cart, User user,
                                                       String shippingName, String shippingAddress, String email) {
        // create blank receipt and save to get ID
        PurchaseReceipt receipt = new PurchaseReceipt();
        receipt.setUser(user);
        receipt.setShippingName(shippingName);
        receipt.setShippingAddress(shippingAddress);
        receipt.setEmail(email);
        receipt.setOrderDateTime(LocalDateTime.now());

        receipt = purchaseReceiptRepository.save(receipt);

        // create items and attach
        double total = 0;
        for (ShoppingCartItem scItem : cart.getItems()) {
            PurchaseReceiptItem prItem = new PurchaseReceiptItem(
                    receipt,
                    scItem.getBook(),
                    scItem.getQuantity(),
                    scItem.getBook().getPrice()
            );
            receipt.addPurchaseReceiptItem(prItem);
            total += prItem.getPricePerBook() * prItem.getQuantity();
            //Decrease the available Inventory Quantity
            bookService.decrementBookInventoryQuantity(scItem.getBook(), prItem.getQuantity());
        }

        // update total and save again
        receipt.setTotalCost(total);
        return purchaseReceiptRepository.save(receipt);
    }


    // METHODS FOR VIEWING RECEIPTS
    @Transactional(readOnly = true)
    public List<PurchaseReceipt> getReceiptsForUser(User user) {
        return purchaseReceiptRepository.findByUserOrderByOrderDateTimeDesc(user);
    }

    @Transactional(readOnly = true)
    public Optional<PurchaseReceipt> getReceiptForUser(Long receiptId, User user) {
        Optional<PurchaseReceipt> receiptOpt = purchaseReceiptRepository.findById(receiptId);
        System.out.println("🔍 getReceiptForUser called with user=" + (user != null ? user.getId() : "null"));
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

    @Transactional
    public PurchaseReceipt saveReceipt(PurchaseReceipt receipt) {
        return purchaseReceiptRepository.save(receipt);
    }
}