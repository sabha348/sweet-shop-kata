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

    public AuthResponse login(String email, String password) { // Return Type is now AuthResponse
        // 1. Find User and Check Password
        User user = userRepository.findByEmail(email)
                .filter(u -> passwordEncoder.matches(password, u.getPassword()))
                .orElseThrow(() -> new RuntimeException("Invalid Login"));

        // 2. Generate Token
        String token = jwtService.generateToken(user.getEmail());
        
        // 3. Return BOTH Token and Role
        return new AuthResponse(token, user.getRole());
    }
}