package com.incubyte.sweetshop;

import java.util.List;

public record OrderResponse(
    Long orderId,
    String orderDate, // sending as String is easier for frontend display
    double totalPrice,
    List<OrderItemResponse> items
) {}