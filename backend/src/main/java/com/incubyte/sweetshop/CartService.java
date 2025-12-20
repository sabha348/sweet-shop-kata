package com.incubyte.sweetshop;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
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
}