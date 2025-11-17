package org.example.repositories;

import org.example.entities.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
class PurchaseReceiptRepositoryTest {

    // inject dependencies
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PurchaseReceiptRepository repository;

    // setup user
    private User user;

    @BeforeEach
    void setup() {
        user = new User();
        user.setUsername("customer");
        user.setPassword("password123");
        user.setUserRole(USER_ROLE.CUSTOMER);
        entityManager.persistAndFlush(user);
    }


    @Test
    void saveReceipt_persistsWithItems() {
        // persist book first because receipt items references it
        Book dune = new Book("123", "Dune", "Desc", List.of("sci-fi"),
                null, "Frank Herbert", "Ace", 10.00);
        entityManager.persistAndFlush(dune);

        // create a receipt linked to the test user
        PurchaseReceipt receipt = new PurchaseReceipt(user);
        PurchaseReceiptItem item =
                new PurchaseReceiptItem(receipt, dune, 2, 10.00);
        receipt.setItems(List.of(item));
        receipt.calculateAndSetTotalCost();

        // save receipt and cascade items
        PurchaseReceipt saved = repository.save(receipt);
        entityManager.flush();

        // confirms receipt row created in DB
        assertThat(saved.getId()).isGreaterThan(0);

        // confirms cascade persistence worked
        assertThat(saved.getItems()).hasSize(1);

        // confirms price * quantity logic correct
        assertThat(saved.getTotalCost()).isEqualTo(20.00);
    }

    @Test
    void findByUserOrderByOrderDateTimeDesc_returnsOnlyUserReceipts() {
        PurchaseReceipt r1 = new PurchaseReceipt(user);
        r1.setOrderDateTime(LocalDateTime.now().minusDays(1));
        repository.save(r1);

        PurchaseReceipt r2 = new PurchaseReceipt(user);
        r2.setOrderDateTime(LocalDateTime.now());
        repository.save(r2);

        List<PurchaseReceipt> results = repository.findByUserOrderByOrderDateTimeDesc(user);

        // confirms receipts are in correct order and only this customers receipts are shown
        assertThat(results).hasSize(2);
        assertThat(results.get(0).getOrderDateTime()).isAfter(results.get(1).getOrderDateTime());
    }
}
