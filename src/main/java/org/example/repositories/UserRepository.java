package org.example.repositories;

import org.example.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findById(long id);
    User findByEmail(String email);
    User findByUsername(String username);
    User findByEmailAndPassword(String email, String password);
    User findByUsernameAndPassword(String username, String password);
}
