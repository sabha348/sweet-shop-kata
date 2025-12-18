package com.incubyte.sweetshop;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(String email, String password) {
        // 1. Hash the password
        String encodedPassword = passwordEncoder.encode(password);
        
        // 2. Create the user with "CUSTOMER" role
        User newUser = new User(email, encodedPassword, "CUSTOMER");
        
        // 3. Save to database
        userRepository.save(newUser);
    }
}