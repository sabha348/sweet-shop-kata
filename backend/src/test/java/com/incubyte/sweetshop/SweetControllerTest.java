package com.incubyte.sweetshop;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SweetController.class)
class SweetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SweetService sweetService;

    @Test
    @WithMockUser
    void should_return_list_of_sweets() throws Exception {
        when(sweetService.getAllSweets()).thenReturn(List.of(
            new Sweet(1L, "Jalebi", 50.0, "url1"),
            new Sweet(2L, "Gulab Jamun", 100.0, "url2")
        ));

        mockMvc.perform(get("/api/sweets"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                    [
                        {"id": 1, "name": "Jalebi", "price": 50.0, "imageUrl": "url1"},
                        {"id": 2, "name": "Gulab Jamun", "price": 100.0, "imageUrl": "url2"}
                    ]
                """));
    }
}