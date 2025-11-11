package org.example.services; // You might need to create this package

import org.example.config.RegistrationConfig;
import org.example.entities.User;
import org.example.entities.USER_ROLE;
import org.example.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerUser(RegistrationConfig dto) {
        // Check if passwords match
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match");
        }

        // Check if username or email already exists
        if (userRepository.findByUsername(dto.getUsername()) != null) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.findByEmail(dto.getEmail()) != null) {
            throw new RuntimeException("Email already exists");
        }


        // Create the new User entity
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setUserRole(USER_ROLE.CUSTOMER);

        // ENCODE the password before saving
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        // Save the user
        userRepository.save(user);
    }
}
