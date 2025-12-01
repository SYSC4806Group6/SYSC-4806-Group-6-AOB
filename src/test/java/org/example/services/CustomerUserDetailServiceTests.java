package org.example.services;

import org.example.entities.USER_ROLE;
import org.example.entities.User;
import org.example.repositories.UserRepository;
import org.example.security.CustomUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomUserDetailServiceTests {

    private UserRepository userRepository;
    private CustomUserDetailService customUserDetailService;

    @BeforeEach
    void setup() throws Exception {
        userRepository = mock(UserRepository.class);

        customUserDetailService = new CustomUserDetailService();

        Field repoField = CustomUserDetailService.class.getDeclaredField("userRepository");
        repoField.setAccessible(true);
        repoField.set(customUserDetailService, userRepository);
    }

    @Test
    void loadUserByUsername_userExists_returnsCustomUserDetails() {
        User user = new User();
        user.setId(1L);
        user.setUsername("john");
        user.setPassword("encodedpass");
        user.setUserRole(USER_ROLE.CUSTOMER);

        when(userRepository.findByUsername("john")).thenReturn(user);

        CustomUserDetails details =
                (CustomUserDetails) customUserDetailService.loadUserByUsername("john");

        assertNotNull(details);
        assertEquals("john", details.getUsername());
        assertEquals("encodedpass", details.getPassword());

        assertTrue(details.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_CUSTOMER")));

        assertEquals(user, details.getUser());
    }

    @Test
    void loadUserByUsername_userDoesNotExist_throwsException() {
        when(userRepository.findByUsername("unknown")).thenReturn(null);

        UsernameNotFoundException ex =
                assertThrows(UsernameNotFoundException.class,
                        () -> customUserDetailService.loadUserByUsername("unknown"));

        assertEquals("User not found with username: unknown", ex.getMessage());
    }
}