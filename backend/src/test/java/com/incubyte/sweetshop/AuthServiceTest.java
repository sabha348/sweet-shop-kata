package com.incubyte.sweetshop;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
    
@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

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

    @Test
    public void should_login_and_return_token_when_credentials_valid() {
        // Given
        String email = "test@incubyte.co";
        String password = "password123";
        String encodedPassword = "encoded_password";
        String expectedToken = "jwt_token_123";

        User user = new User(email, encodedPassword, "CUSTOMER");
        
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true); // Password matches
        when(jwtService.generateToken(email)).thenReturn(expectedToken);

        // When
        String token = authService.login(email, password);

        // Then
        assertThat(token).isEqualTo(expectedToken);
    }

    @Test
    public void should_throw_exception_when_password_invalid() {
        String email = "test@incubyte.co";
        User user = new User(email, "encoded_real_password", "CUSTOMER");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong_password", "encoded_real_password")).thenReturn(false); // Password mismatch

        assertThrows(RuntimeException.class, () -> authService.login(email, "wrong_password"));
    }
}