package com.incubyte.sweetshop;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add")
    public void addToCart(@AuthenticationPrincipal UserDetails userDetails, @RequestBody AddToCartRequest request) {
        cartService.addToCart(userDetails.getUsername(), request);
    }

    @GetMapping
    public CartResponse getCart(@AuthenticationPrincipal UserDetails userDetails) {
        return cartService.getCart(userDetails.getUsername());
    }
}