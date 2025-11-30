package org.example.services;

import org.example.config.RegistrationConfig;
import org.example.entities.USER_ROLE;
import org.example.entities.User;
import org.example.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private RegistrationConfig validDto;

    @BeforeEach
    void setUp() {
        validDto = new RegistrationConfig(
                "newuser",
                "test@example.com",
                "password123",
                "password123",
                USER_ROLE.CUSTOMER
        );
    }

    @Test
    void registerUser_Success() {
        when(userRepository.findByUsername("newuser")).thenReturn(null);
        when(userRepository.findByEmail("test@example.com")).thenReturn(null);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPass");

        userService.registerUser(validDto);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void registerUser_ThrowsException_WhenPasswordsDoNotMatch() {
        validDto.setConfirmPassword("mismatch");

        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.registerUser(validDto);
        });

        assertEquals("Passwords do not match", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void registerUser_ThrowsException_WhenUsernameExists() {
        when(userRepository.findByUsername("newuser")).thenReturn(new User());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.registerUser(validDto);
        });

        assertEquals("Username already exists", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void registerUser_ThrowsException_WhenEmailExists() {
        when(userRepository.findByUsername("newuser")).thenReturn(null);
        when(userRepository.findByEmail("test@example.com")).thenReturn(new User());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.registerUser(validDto);
        });

        assertEquals("Email already exists", exception.getMessage());
    }
}