package com.incubyte.sweetshop;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public void register(String email, String password) {
        // 1. Hash the password
        String encodedPassword = passwordEncoder.encode(password);
        
        // 2. Create the user with "CUSTOMER" role
        User newUser = new User(email, encodedPassword, "CUSTOMER");
        
        // 3. Save to database
        userRepository.save(newUser);
    }

    public String login(String email, String password) {
        return userRepository.findByEmail(email)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .map(user -> jwtService.generateToken(user.getEmail()))
                .orElseThrow(() -> new RuntimeException("Invalid Login"));
    }
}