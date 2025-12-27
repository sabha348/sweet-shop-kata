package com.incubyte.sweetshop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.util.List;

import org.springframework.context.annotation.Import;
import com.incubyte.sweetshop.config.SecurityConfig;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.incubyte.sweetshop.JwtAuthenticationFilter;

import jakarta.servlet.FilterChain;

@WebMvcTest(SweetController.class)
@Import(SecurityConfig.class)
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

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void should_allow_admin_to_add_sweet() throws Exception {
        Sweet newSweet = new Sweet(null, "Kaju Katli", 1000L, 50, "url");
        
        mockMvc.perform(post("/api/sweets") // <--- Update this URL
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newSweet)))
                .andExpect(status().isOk());

        verify(sweetService).addSweet(any(Sweet.class));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void should_deny_non_admin_from_adding_sweet() throws Exception {
        Sweet newSweet = new Sweet(null, "Hacked Sweet", 0L, 0, "url");

        mockMvc.perform(post("/api/sweets") // <--- Update this URL
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newSweet)))
                .andExpect(status().isForbidden());

        verify(sweetService, times(0)).addSweet(any());
    }
}