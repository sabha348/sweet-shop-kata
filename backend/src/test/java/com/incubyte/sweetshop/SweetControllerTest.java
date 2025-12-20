package com.incubyte.sweetshop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.incubyte.sweetshop.JwtAuthenticationFilter;

import jakarta.servlet.FilterChain;

@WebMvcTest(SweetController.class)
class SweetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SweetService sweetService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setup() throws Exception {
        // Allow request to pass through filter
        doAnswer(inv -> {
            ((FilterChain) inv.getArgument(2)).doFilter(inv.getArgument(0), inv.getArgument(1));
            return null;
        }).when(jwtAuthenticationFilter).doFilter(any(), any(), any());
    }

    @Test
    @WithMockUser
    void should_return_list_of_sweets() throws Exception {
        when(sweetService.getAllSweets()).thenReturn(List.of(
            new Sweet(1L, "Jalebi", 5000L, 10, "url1"),
            new Sweet(2L, "Gulab Jamun", 10000L, 20, "url2")
        ));

        mockMvc.perform(get("/api/sweets"))
                .andExpect(status().isOk())
                // Verify basic fields match
                .andExpect(content().json("""
                    [
                        {"id": 1, "name": "Jalebi", "price": 5000, "quantity": 10, "imageUrl": "url1"},
                        {"id": 2, "name": "Gulab Jamun", "price": 10000, "quantity": 20, "imageUrl": "url2"}
                    ]
                """));
    }
}