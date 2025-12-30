package com.incubyte.sweetshop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing; // 🟢 Added for void methods
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content; // 🟢 Added to check response text

import java.util.List;

import com.incubyte.sweetshop.JwtAuthenticationFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebMvcTest(CartController.class)
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CartService cartService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setup() throws Exception {
        // CRITICAL FIX: Tell the mock filter to let the request pass through!
        doAnswer(invocation -> {
            HttpServletRequest request = invocation.getArgument(0);
            HttpServletResponse response = invocation.getArgument(1);
            FilterChain chain = invocation.getArgument(2);
            chain.doFilter(request, response); // Forward the request
            return null;
        }).when(jwtAuthenticationFilter).doFilter(any(), any(), any());
    }

    @Test
    @WithMockUser(username = "test@example.com") // Simulate a logged-in user
    void should_add_item_to_cart() throws Exception {
        mockMvc.perform(post("/api/cart/add")
                        .with(csrf()) // Required for tests even if disabled in config
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "sweetId": 1,
                                "quantity": 2
                            }
                        """))
                .andExpect(status().isOk());

        // Verify the service was called with the correct user email
        verify(cartService).addToCart(eq("test@example.com"), any(AddToCartRequest.class));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void should_get_cart_for_logged_in_user() throws Exception {
        // Mock the response
        CartResponse mockResponse = new CartResponse(
            1L,
            List.of(new CartItemResponse(10L, "Jalebi", 50.0, 2, 100.0)),
            100.0
        );

        when(cartService.getCart("test@example.com")).thenReturn(mockResponse);

        mockMvc.perform(get("/api/cart")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.grandTotal").value(100.0))
                .andExpect(jsonPath("$.items[0].sweetName").value("Jalebi"));
    }

    @Test
    @WithMockUser(username = "test@example.com", roles = "CUSTOMER") 
    void should_remove_item_from_cart_successfully() throws Exception {
        Long cartItemId = 5L;
        String userEmail = "test@example.com";

        // Mock the service to do nothing when delete is called
        doNothing().when(cartService).removeFromCart(userEmail, cartItemId);

        // Act & Assert
        mockMvc.perform(delete("/api/cart/{cartItemId}", cartItemId)
                        .with(csrf())) // Required for state-changing operations
                .andExpect(status().isOk());
        // Verify the service was called with the correct user email and item ID
        verify(cartService).removeFromCart(userEmail, cartItemId);
    }
}