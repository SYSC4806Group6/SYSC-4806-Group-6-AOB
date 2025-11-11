package org.example.sampledata;

import org.example.entities.USER_ROLE;
import org.example.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SampleUsers {
    @Autowired
    private PasswordEncoder passwordEncoder;
    public List<User> getUsers() {
        User admin = new User(
                "admin@gmail.com",
                "admin",
                "",
                USER_ROLE.ADMIN
        );
        admin.setPassword(passwordEncoder.encode("password123"));
        User customer = new User(
                "customer@gmail.com",
                "customer",
                "",
                USER_ROLE.CUSTOMER
        );
        customer.setPassword(passwordEncoder.encode("password123"));

        return List.of(
                admin, customer
        );
    }
}
