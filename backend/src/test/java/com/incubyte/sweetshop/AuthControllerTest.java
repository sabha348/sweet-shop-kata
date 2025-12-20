package com.incubyte.sweetshop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.incubyte.sweetshop.config.SecurityConfig;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean; // New in Spring Boot 3.4+
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.incubyte.sweetshop.JwtAuthenticationFilter;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class) // Loads your security rules
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean // Replaces @MockitoBean in newer Spring versions
    private AuthService authService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter; // For SecurityConfig

    @MockitoBean
    private JwtService jwtService; // For AuthController

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    void setup() throws Exception {
        doAnswer(invocation -> {
            HttpServletRequest request = invocation.getArgument(0);
            HttpServletResponse response = invocation.getArgument(1);
            FilterChain chain = invocation.getArgument(2);
            chain.doFilter(request, response); // Allow request to proceed to Controller
            return null;
        }).when(jwtAuthenticationFilter).doFilter(any(), any(), any());
    }    

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
                .andExpect(status().isCreated());

        // Verify the service was actually called
        verify(authService).register("newuser@test.com", "password123");
    }

    @Test
    void should_login_user_and_return_token() throws Exception {
        // Given
        LoginRequest request = new LoginRequest("test@incubyte.co", "password123");
        String expectedToken = "jwt_token_123";

        // Mock the service behavior
        when(authService.login(request.email(), request.password())).thenReturn(expectedToken);

        // When/Then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request))) // Convert object to JSON
                .andExpect(status().isOk())
                .andExpect(content().string(expectedToken));
    }
}