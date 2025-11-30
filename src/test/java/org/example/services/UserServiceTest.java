package org.example.services;

import org.example.config.RegistrationConfig;
import org.example.entities.USER_ROLE;
import org.example.entities.User;
import org.example.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private RegistrationConfig dto;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        dto = new RegistrationConfig(
                "newUser",
                "email@example.com",
                "password123",
                "password123",
                USER_ROLE.CUSTOMER
        );

    }

    @Test
    void registerUser_success_savesUser() {
        given(userRepository.findByUsername("newUser")).willReturn(null);
        given(userRepository.findByEmail("email@example.com")).willReturn(null);
        given(passwordEncoder.encode("password123")).willReturn("encodedPass");

        userService.registerUser(dto);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());

        User saved = captor.getValue();
        assertEquals("newUser", saved.getUsername());
        assertEquals("email@example.com", saved.getEmail());
        assertEquals("encodedPass", saved.getPassword());
        assertEquals(USER_ROLE.CUSTOMER, saved.getUserRole());
    }

    @Test
    void registerUser_passwordMismatch_throwsException() {
        dto.setConfirmPassword("different");

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> userService.registerUser(dto));

        assertEquals("Passwords do not match", ex.getMessage());
    }

    @Test
    void registerUser_usernameExists_throwsException() {
        given(userRepository.findByUsername("newUser")).willReturn(new User());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> userService.registerUser(dto));

        assertEquals("Username already exists", ex.getMessage());
    }

    @Test
    void registerUser_emailExists_throwsException() {
        given(userRepository.findByUsername("newUser")).willReturn(null);
        given(userRepository.findByEmail("email@example.com")).willReturn(new User());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> userService.registerUser(dto));

        assertEquals("Email already exists", ex.getMessage());
    }
}
