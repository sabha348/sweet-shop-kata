package com.incubyte.sweetshop;

import org.junit.jupiter.api.Test;
import com.incubyte.sweetshop.config.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean; // New in Spring Boot 3.4+
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class) // Loads your security rules
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean // Replaces @MockBean in newer Spring versions
    private AuthService authService;

    @Test
    public void should_register_new_user() throws Exception {
        // JSON payload mimicking what the frontend sends
        String requestBody = """
            {
                "email": "newuser@test.com",
                "password": "password123"
            }
        """;

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated()); // Expect HTTP 201

        // Verify the service was actually called
        verify(authService).register("newuser@test.com", "password123");
    }
}