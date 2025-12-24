package com.incubyte.sweetshop;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository, CartRepository cartRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
    }

    public void checkout(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Cart cart = cartRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Cart is empty"));

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cannot checkout an empty cart");
        }

        // Calculate Total
        double total = cart.getItems().stream()
                .mapToDouble(item -> item.getSweet().getPrice() * item.getQuantity())
                .sum();

        // Create Order
        Order order = new Order(user, LocalDateTime.now(), total);

        // Move items
        for (CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = new OrderItem(
                    cartItem.getSweet(),
                    cartItem.getQuantity(),
                    (double) cartItem.getSweet().getPrice()
            );
            order.addOrderItem(orderItem);
        }

        orderRepository.save(order);
        
        // Clear Cart
        cart.getItems().clear();
        cartRepository.save(cart);
    }
}