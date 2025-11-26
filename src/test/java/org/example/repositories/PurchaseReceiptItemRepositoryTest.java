package org.example.repositories;

import org.example.entities.Book;
import org.example.entities.PurchaseReceipt;
import org.example.entities.PurchaseReceiptItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class PurchaseReceiptItemRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PurchaseReceiptItemRepository purchaseReceiptItemRepository;

    private Book book;
    private PurchaseReceipt purchaseReceipt;
    private PurchaseReceiptItem pri1;

    @BeforeEach
    void setUp() {
        book = new Book(
                "978-0321765723",
                "The Lord of the Rings",
                "A classic fantasy novel.",
                new ArrayList<>(List.of("Fantasy", "Adventure")),
                "url.jpg",
                "J.R.R. Tolkien",
                "Allen & Unwin",
                22.99
        );
        purchaseReceipt = new PurchaseReceipt();
        pri1 = new PurchaseReceiptItem(purchaseReceipt, book, 1, book.getPrice());
    }

    @Test
    public void testSavePurchaseReceiptItem() {
        PurchaseReceiptItem savedUser = purchaseReceiptItemRepository.save(pri1);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isEqualTo(pri1.getId());
    }

    @Test
    public void testFindById() {
        entityManager.persist(book);
        entityManager.persist(purchaseReceipt);
        entityManager.persistAndFlush(pri1);

        PurchaseReceiptItem foundItem = purchaseReceiptItemRepository.findById(pri1.getId());

        assertNotNull(foundItem);
        assertThat(foundItem.getId()).isEqualTo(pri1.getId());
    }

}