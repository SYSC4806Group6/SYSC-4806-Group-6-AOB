package org.example.controllers;

import org.example.config.RegistrationConfig;
import org.example.entities.USER_ROLE;
import org.example.services.CustomUserDetailService;
import jakarta.validation.Valid;
import org.example.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
public class RegistrationController {

    @Autowired
    private UserService userService;

    // This method handles GET requests to /register
    // It displays the registration form
    @GetMapping
    public String showRegistrationForm(Model model) {
        // Add an empty DTO to the model for the form to bind to
        model.addAttribute("userDto", new RegistrationConfig("", "", "", "", USER_ROLE.CUSTOMER));

        return "register"; // This must match the name of your HTML file
    }

    // This method handles POST requests to /register
    // It processes the form submission
    @PostMapping
    public String processRegistration(
            @Valid @ModelAttribute("userDto") RegistrationConfig dto,
            BindingResult result,
            Model model) {

        // Check for validation errors
        if (result.hasErrors()) {
            return "register"; // Return to the form if there are errors
        }

        // Check for password mismatch manually (since it's not a field-level validation)
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "Match", "Passwords do not match");
        }

        // If there are still errors (like password mismatch), return to form
        if (result.hasErrors()) {
            return "register";
        }

        try {
            userService.registerUser(dto);
        } catch (RuntimeException e) {
            // Handle exceptions (like "Username already exists")
            model.addAttribute("roles", new USER_ROLE[]{USER_ROLE.CUSTOMER, USER_ROLE.ADMIN});
            result.rejectValue("username", "Exists", e.getMessage());
            return "register";
        }

        // Redirect to the login page with a success message
        return "redirect:/login?register=success";
    }
}
