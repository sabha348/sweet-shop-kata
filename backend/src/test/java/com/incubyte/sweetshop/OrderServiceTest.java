package com.incubyte.sweetshop;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OrderServiceTest {

    private final OrderRepository orderRepository = mock(OrderRepository.class);
    private final CartRepository cartRepository = mock(CartRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    
    private final OrderService orderService = new OrderService(orderRepository, cartRepository, userRepository);

    @Test
    void should_checkout_successfully() {
        // Arrange
        String email = "test@example.com";
        User user = new User(email, "password123", "ROLE_USER");
        
        // Create a Cart with 1 item (Price 100 * Qty 2 = 200)
        Cart cart = new Cart(user);
        Sweet sweet = new Sweet(1L, "Jalebi", 100L, 10, "url");
        cart.getItems().add(new CartItem(cart, sweet, 2));

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(cartRepository.findByUserEmail(email)).thenReturn(Optional.of(cart));

        // Act
        orderService.checkout(email);

        // Assert
        // 1. Capture the saved Order to check its contents
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderCaptor.capture());
        
        Order savedOrder = orderCaptor.getValue();
        assertThat(savedOrder.getTotalPrice()).isEqualTo(200.0);
        assertThat(savedOrder.getItems()).hasSize(1);
        
        // 2. Verify the Cart was cleared
        assertThat(cart.getItems()).isEmpty();
    }
}