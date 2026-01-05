package com.incubyte.sweetshop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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
            new Sweet(1L, "Jalebi", 5000L, 10, "url1", "General"),
            new Sweet(2L, "Gulab Jamun", 10000L, 20, "url2", "General")
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
        Sweet newSweet = new Sweet(null, "Kaju Katli", 1000L, 50, "url", "General");
        
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
        Sweet newSweet = new Sweet(null, "Hacked Sweet", 0L, 0, "url", "General");

        mockMvc.perform(post("/api/sweets")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newSweet)))
                .andExpect(status().isForbidden());

        verify(sweetService, times(0)).addSweet(any());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void should_update_sweet_successfully() throws Exception {
        // Arrange
        Long sweetId = 1L;
        
        // 1. Create Sweet using your actual Constructor: (id, name, price, quantity, imageUrl)
        Sweet updatedSweet = new Sweet(sweetId, "Updated Laddu", 200L, 50, "https://new-image.com", "General");

        // Mock the service behavior
        Mockito.when(sweetService.updateSweet(Mockito.eq(sweetId), Mockito.any(Sweet.class)))
               .thenReturn(updatedSweet);

        // Act & Assert
        mockMvc.perform(put("/api/sweets/{id}", sweetId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(updatedSweet))
                .with(csrf())) 
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Laddu"))
                .andExpect(jsonPath("$.price").value(200)) // Check for Integer/Long
                .andExpect(jsonPath("$.quantity").value(50)); // Check for Quantity
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void should_delete_sweet_successfully() throws Exception {
        // Arrange
        Long sweetId = 1L;
        
        Mockito.doNothing().when(sweetService).deleteSweet(sweetId);

        // Act & Assert
        mockMvc.perform(delete("/api/sweets/{id}", sweetId)
                .with(csrf()))
                .andExpect(status().isNoContent()); // 204
        
        Mockito.verify(sweetService).deleteSweet(sweetId);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void should_create_sweet_with_category() throws Exception {
        String jsonRequest = """
            {
                "name": "Gulab Jamun",
                "price": 50,
                "quantity": 10,
                "imageUrl": "http://image.com",
                "category": "Milk-Based"
            }
        """;

        Sweet mockSweet = new Sweet(1L, "Gulab Jamun", 50L, 10, "http://image.com", "Milk-Based");
        
        when(sweetService.addSweet(any(Sweet.class))).thenReturn(mockSweet);

        mockMvc.perform(post("/api/sweets")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.category").value("Milk-Based"));
    }
}