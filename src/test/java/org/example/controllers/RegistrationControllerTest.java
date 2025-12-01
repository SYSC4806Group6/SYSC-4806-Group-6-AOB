package org.example.controllers;

import org.example.config.RegistrationConfig;
import org.example.config.SecurityConfig;
import org.example.entities.USER_ROLE;
import org.example.services.CustomUserDetailService;
import org.example.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RegistrationController.class)
@Import(SecurityConfig.class)
class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    // Needed because SecurityConfig loads authentication
    @MockBean
    private CustomUserDetailService customUserDetailService;

    @Test
    void showRegistrationForm_displaysForm() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("userDto"));
    }

    @Test
    void processRegistration_success_redirectsToLogin() throws Exception {
        Mockito.doNothing().when(userService).registerUser(any(RegistrationConfig.class));

        mockMvc.perform(post("/register")
                        .with(csrf())
                        .param("username", "newuser")
                        .param("password", "password123")
                        .param("confirmPassword", "password123")
                        .param("email", "email@example.com")
                        .param("address", "123 Road")
                        .param("role", USER_ROLE.CUSTOMER.name()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?register=success"));
    }

    @Test
    void processRegistration_validationErrors_returnsForm() throws Exception {
        mockMvc.perform(post("/register")
                        .with(csrf())
                        .param("username", "")        // invalid
                        .param("password", "pass")
                        .param("confirmPassword", "pass")
                        .param("email", "")           // invalid
                        .param("address", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeHasErrors("userDto"));
    }

    @Test
    void processRegistration_passwordMismatch_returnsForm() throws Exception {
        mockMvc.perform(post("/register")
                        .with(csrf())
                        .param("username", "user")
                        .param("password", "abc123")
                        .param("confirmPassword", "xyz999") // mismatch
                        .param("email", "email@example.com")
                        .param("address", "Street"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeHasErrors("userDto"));
    }

    @Test
    void processRegistration_usernameExists_returnsForm() throws Exception {
        willThrow(new RuntimeException("Username already exists"))
                .given(userService).registerUser(any());

        mockMvc.perform(post("/register")
                        .with(csrf())
                        .param("username", "existing")
                        .param("password", "pass123")
                        .param("confirmPassword", "pass123")
                        .param("email", "email@example.com")
                        .param("address", "Street"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeHasErrors("userDto"))
                .andExpect(model().attributeExists("roles"));
    }
}
