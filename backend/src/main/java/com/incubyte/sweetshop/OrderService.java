package com.incubyte.sweetshop;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final SweetRepository sweetRepository;

    public OrderService(OrderRepository orderRepository, CartRepository cartRepository, UserRepository userRepository, SweetRepository sweetRepository) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.sweetRepository = sweetRepository;
    }

    public void checkout(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Cart cart = cartRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Cart is empty"));

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cannot checkout an empty cart");
        }

        // 🟢 3. Validate Stock & Calculate Total
        double total = 0;
        for (CartItem item : cart.getItems()) {
            Sweet sweet = item.getSweet();
            
            // Check if we have enough stock
            if (sweet.getQuantity() < item.getQuantity()) {
                throw new RuntimeException("Not enough stock for sweet: " + sweet.getName());
            }
            
            // Reduce stock
            sweet.setQuantity(sweet.getQuantity() - item.getQuantity());
            sweetRepository.save(sweet); // Save the new quantity to DB

            total += (sweet.getPrice() * item.getQuantity());
        }

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

    
    public List<OrderResponse> getAllOrders(String userEmail) {
        return orderRepository.findByUserEmailOrderByOrderDateDesc(userEmail)
                .stream()
                .map(order -> new OrderResponse(
                        order.getId(),
                        order.getOrderDate().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                        order.getTotalPrice(),
                        order.getItems().stream()
                                .map(item -> new OrderItemResponse(
                                        item.getSweet().getName(),
                                        item.getQuantity(),
                                        item.getPriceAtPurchase()
                                ))
                                .toList()
                ))
                .toList();
    }
}