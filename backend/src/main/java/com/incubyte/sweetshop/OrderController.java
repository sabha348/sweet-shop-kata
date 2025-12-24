package com.incubyte.sweetshop;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/checkout")
    public void checkout(@AuthenticationPrincipal UserDetails userDetails) {
        orderService.checkout(userDetails.getUsername());
    }

    @GetMapping
    public List<OrderResponse> getOrders(@AuthenticationPrincipal UserDetails userDetails) {
        return orderService.getAllOrders(userDetails.getUsername());
    }
}