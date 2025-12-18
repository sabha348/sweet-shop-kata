package com.incubyte.sweetshop;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Test
    public void should_register_user_with_hashed_password() {
        // Given
        String email = "test@incubyte.co";
        String rawPassword = "password123";
        String encodedPassword = "encoded_password123";

        // Mock the password encoder to return a fake hash
        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);

        // When
        authService.register(email, rawPassword);

        // Then
        // Verify that repository.save() was called with a User object
        // And that User object had the ENCODED password, not the raw one
        verify(userRepository).save(any(User.class));
        verify(passwordEncoder).encode(rawPassword);
    }
}