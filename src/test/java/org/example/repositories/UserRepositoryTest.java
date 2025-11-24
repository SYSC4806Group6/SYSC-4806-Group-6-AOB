package org.example.repositories;

import org.example.entities.USER_ROLE;
import org.example.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User user1;

    @BeforeEach
    void setUp() {
        user1 = new User(
                "johnDoe@example.com",
                "johndoe",
                "password",
                USER_ROLE.CUSTOMER
        );
    }

    @Test
    public void testSaveUser() {
        User savedUser = userRepository.save(user1);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isEqualTo(user1.getId());
    }

    @Test
    public void testFindById() {
        entityManager.persistAndFlush(user1);

        User foundUser = userRepository.findById(user1.getId());

        assertNotNull(foundUser);
        assertThat(foundUser.getId()).isEqualTo(user1.getId());
    }

    @Test
    public void testFindByEmail() {
        entityManager.persistAndFlush(user1);
        User foundUser = userRepository.findByEmail("johnDoe@example.com");
        assertNotNull(foundUser);
        assertThat(foundUser.getUsername()).isEqualTo("johndoe");
    }

    @Test
    public void testFindByUsername() {
        entityManager.persistAndFlush(user1);
        User foundUser = userRepository.findByUsername("johndoe");
        assertNotNull(foundUser);
        assertThat(foundUser.getUsername()).isEqualTo("johndoe");
    }

    @Test
    public void testFindByEmailAndPassword() {
        entityManager.persistAndFlush(user1);
        User foundUser = userRepository.findByEmailAndPassword("johnDoe@example.com", "password");
        assertNotNull(foundUser);
        assertThat(foundUser.getUsername()).isEqualTo("johndoe");
    }

    @Test
    public void testFindByUsernameAndPassword() {
        entityManager.persistAndFlush(user1);
        User foundUser = userRepository.findByUsernameAndPassword("johndoe", "password");
        assertNotNull(foundUser);
        assertThat(foundUser.getUsername()).isEqualTo("johndoe");
    }
}