package org.example.services;

import org.example.entities.*;
import org.example.repositories.PurchaseReceiptRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class PurchaseReceiptServiceTest {

    @Mock
    private PurchaseReceiptRepository purchaseReceiptRepository;

    @Mock
    private BookService bookService;

    @InjectMocks
    private PurchaseReceiptService purchaseReceiptService;

    private ShoppingCart cart;
    private User user;
    private Book dune;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);

        dune = new Book("123", "Dune", "Sci-fi", List.of(), null,
                "Frank Herbert", "Ace", 10.0);

        ShoppingCartItem item = new ShoppingCartItem();
        item.setBook(dune);
        item.setQuantity(2);

        cart = new ShoppingCart();
        cart.addShoppingCartItem(item);
    }

    @Test
    void getReceiptsForUser_returnsListFromRepository() {
        PurchaseReceipt r1 = new PurchaseReceipt();
        PurchaseReceipt r2 = new PurchaseReceipt();

        when(purchaseReceiptRepository.findByUserOrderByOrderDateTimeDesc(user))
                .thenReturn(List.of(r1, r2));

        List<PurchaseReceipt> result = purchaseReceiptService.getReceiptsForUser(user);

        assertEquals(2, result.size());
    }

    @Test
    void getReceiptForUser_wrongUser_returnsEmpty() {
        User other = new User();
        other.setId(2L);

        PurchaseReceipt receipt = new PurchaseReceipt();
        receipt.setUser(other);

        when(purchaseReceiptRepository.findById(100L))
                .thenReturn(Optional.of(receipt));

        Optional<PurchaseReceipt> result = purchaseReceiptService.getReceiptForUser(100L, user);

        assertTrue(result.isEmpty());
    }

    @Test
    void getReceiptForUser_notFound_returnsEmpty() {
        when(purchaseReceiptRepository.findById(100L))
                .thenReturn(Optional.empty());

        Optional<PurchaseReceipt> result = purchaseReceiptService.getReceiptForUser(100L, user);

        assertTrue(result.isEmpty());
    }

    @Test
    void saveReceipt_savesThroughRepository() {
        PurchaseReceipt receipt = new PurchaseReceipt();
        when(purchaseReceiptRepository.save(receipt)).thenReturn(receipt);

        PurchaseReceipt result = purchaseReceiptService.saveReceipt(receipt);

        assertEquals(receipt, result);
        verify(purchaseReceiptRepository).save(receipt);
    }
}