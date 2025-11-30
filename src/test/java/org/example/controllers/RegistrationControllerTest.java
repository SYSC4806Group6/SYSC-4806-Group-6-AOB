package org.example.controllers;

import org.example.config.SecurityConfig;
import org.example.entities.USER_ROLE;
import org.example.services.CustomUserDetailService;
import org.example.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
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

    @MockBean
    private CustomUserDetailService customUserDetailService;

    @Test
    void showRegistrationForm_ReturnsRegisterView() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("userDto"));
    }

    @Test
    void processRegistration_Successful_RedirectsToLogin() throws Exception {
        mockMvc.perform(post("/register")
                        .with(csrf())
                        .param("username", "newuser")
                        .param("email", "test@test.com")
                        .param("password", "pass123")
                        .param("confirmPassword", "pass123")
                        .param("role", "CUSTOMER"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?register=success"));

        verify(userService).registerUser(any());
    }

    @Test
    void processRegistration_PasswordMismatch_ReturnsToForm() throws Exception {
        mockMvc.perform(post("/register")
                        .with(csrf())
                        .param("username", "newuser")
                        .param("email", "test@test.com")
                        .param("password", "pass123")
                        .param("confirmPassword", "WRONG_PASS") // Mismatch
                        .param("role", "CUSTOMER"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeHasFieldErrorCode("userDto", "confirmPassword", "Match"));
    }

    @Test
    void processRegistration_ServiceException_ReturnsToForm() throws Exception {
        // Simulate "Username already exists" exception from Service
        doThrow(new RuntimeException("Username already exists"))
                .when(userService).registerUser(any());

        mockMvc.perform(post("/register")
                        .with(csrf())
                        .param("username", "duplicateUser")
                        .param("email", "test@test.com")
                        .param("password", "pass123")
                        .param("confirmPassword", "pass123")
                        .param("role", "CUSTOMER"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeHasFieldErrorCode("userDto", "username", "Exists"));
    }
}
