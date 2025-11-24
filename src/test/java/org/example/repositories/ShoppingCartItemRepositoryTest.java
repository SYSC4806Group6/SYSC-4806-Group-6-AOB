package org.example.repositories;

import org.example.entities.Book;
import org.example.entities.ShoppingCart;
import org.example.entities.ShoppingCartItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ShoppingCartItemRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ShoppingCartItemRepository shoppingCartItemRepository;

    private Book book;
    private ShoppingCart shoppingCart;
    private ShoppingCartItem sci1;

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
        shoppingCart = new ShoppingCart();
        sci1 = new ShoppingCartItem(shoppingCart, book, 1);
    }

    @Test
    public void testSaveShoppingCartItem() {
        ShoppingCartItem savedItem = shoppingCartItemRepository.save(sci1);

        assertThat(savedItem).isNotNull();
        assertThat(savedItem.getId()).isEqualTo(sci1.getId());
    }

    @Test
    public void testFindById() {
        entityManager.persist(book);
        entityManager.persist(shoppingCart);
        entityManager.persistAndFlush(sci1);

        Optional<ShoppingCartItem> foundCartItemOpt = shoppingCartItemRepository.findById(sci1.getId());

        assertThat(foundCartItemOpt).isPresent();
        assertThat(foundCartItemOpt.get().getId()).isEqualTo(sci1.getId());
    }

}