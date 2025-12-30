package com.incubyte.sweetshop;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CartService {
    private final CartRepository cartRepository;
    private final SweetRepository sweetRepository;
    private final UserRepository userRepository;

    public CartService(CartRepository cartRepository, SweetRepository sweetRepository, UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.sweetRepository = sweetRepository;
        this.userRepository = userRepository;
    }

    public void addToCart(String userEmail, AddToCartRequest request) {
        // 1. Find or Create Cart for User
        Cart cart = cartRepository.findByUserEmail(userEmail)
                .orElseGet(() -> {
                    User user = userRepository.findByEmail(userEmail)
                            .orElseThrow(() -> new RuntimeException("User not found"));
                    Cart newCart = new Cart(user);
                    return cartRepository.save(newCart);
                });

        // 2. Find Sweet
        Sweet sweet = sweetRepository.findById(request.sweetId())
                .orElseThrow(() -> new RuntimeException("Sweet not found"));

        // 3. Check if item exists in cart
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getSweet().getId().equals(sweet.getId())) // ERROR HERE? No, Sweet needs getSweet() in CartItem
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + request.quantity());
        } else {
            cart.getItems().add(new CartItem(cart, sweet, request.quantity()));
        }
        
        cartRepository.save(cart);
    }

    public CartResponse getCart(String userEmail) {
        // 1. Fetch Cart (or create empty if not exists)
        Cart cart = cartRepository.findByUserEmail(userEmail)
                .orElse(new Cart()); // Return empty cart if none found

        // 2. Map Items to DTOs
        List<CartItemResponse> itemResponses = cart.getItems().stream()
                .map(item -> new CartItemResponse(
                        item.getSweet().getId(),
                        item.getSweet().getName(),
                        item.getSweet().getPrice(), // Assumes price is stored as double or long
                        item.getQuantity(),
                        item.getSweet().getPrice() * item.getQuantity()
                ))
                .toList();

        // 3. Calculate Grand Total
        double grandTotal = itemResponses.stream()
                .mapToDouble(CartItemResponse::totalPrice)
                .sum();

        return new CartResponse(cart.getId(), itemResponses, grandTotal);
    }

    // Add this method to CartService.java
    public void removeFromCart(String userEmail, Long sweetId) {
        // 1. Get the User's Cart
        Cart cart = cartRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Cart not found for user: " + userEmail));

        // 2. Remove the item that matches the Sweet ID
        // 'removeIf' returns true if an item was actually removed
        boolean removed = cart.getItems().removeIf(item -> item.getSweet().getId().equals(sweetId));

        if (!removed) {
            throw new RuntimeException("Item not found in cart");
        }

        // 3. Save the Cart (Hibernate will delete the orphaned item from DB)
        cartRepository.save(cart);
    }
}