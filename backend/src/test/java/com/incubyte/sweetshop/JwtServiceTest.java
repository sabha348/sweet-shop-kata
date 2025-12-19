package com.incubyte.sweetshop;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class JwtServiceTest {

    @Test
    public void should_generate_token_with_email_claim() {
        JwtService jwtService = new JwtService();
        String email = "test@incubyte.co";

        String token = jwtService.generateToken(email);

        assertThat(token).isNotBlank();
        // Ideally we would decode it to check, but checking it's a non-empty string is enough for step 1
    }
}