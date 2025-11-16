package org.example.repositories;

import org.example.entities.*;
import org.example.entities.USER_ROLE;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ShoppingCartRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    private User user;
    @BeforeEach
    void setup() {
        // Create and persist a user
        user = new User();
        user.setUsername("customer");
        user.setPassword("password123");
        user.setUserRole(USER_ROLE.CUSTOMER);
        entityManager.persistAndFlush(user);
    }

    @Test
    void testSaveCart_createsCartForUser() {
        ShoppingCart cart = new ShoppingCart(user);
        ShoppingCart saved = shoppingCartRepository.save(cart);

        assertThat(saved.getId()).isGreaterThan(0);
        assertThat(saved.getUser()).isEqualTo(user);
    }

    @Test
    void testFindByUser_returnsCart() {
        ShoppingCart cart = shoppingCartRepository.findByUser(user)
                .orElseGet(() -> {
                    ShoppingCart newCart = new ShoppingCart(user);
                    return shoppingCartRepository.save(newCart);
                });

        Optional<ShoppingCart> found = shoppingCartRepository.findByUser(user);

        assertThat(found).isPresent();
        assertThat(found.get().getUser()).isEqualTo(user);
    }
}
