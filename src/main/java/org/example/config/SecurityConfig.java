package org.example.config;

import org.example.services.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Encrypt passwords
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(CustomUserDetailsService userDetailsService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        // Allow access to public pages like home, book list, and details
                        .requestMatchers("/", "/books", "/books/**").permitAll()
                        // Allow access to static resources (if you have them)
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                        // Require ADMIN role for admin pages
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        // All other requests (like /cart) require authentication
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        // Use a custom login page
                        .loginPage("/login")
                        // This is the URL the form will POST to
                        .loginProcessingUrl("/login")
                        // Redirect to the book list on successful login
                        .defaultSuccessUrl("/books", true)
                        // Allow everyone to see the login page
                        .permitAll()
                )
                .logout(logout -> logout
                        // Configure logout URL
                        .logoutUrl("/logout")
                        // On successful logout, redirect to the login page with a ?logout parameter
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }
}