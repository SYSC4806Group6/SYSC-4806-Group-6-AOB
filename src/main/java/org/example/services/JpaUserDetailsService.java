package org.example.services;

import org.example.entities.User;
import org.example.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JpaUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 1. Get the Optional "box" from the repository
        User user = userRepository.findByUsername(username);

        // 2. Check if the box has a user in it
        if (user != null) {
            return user;
        } else {
            // 3. If the box is empty, throw the required exception
            throw new UsernameNotFoundException("User not found: " + username);
        }
    }
}
