package com.incubyte.sweetshop;

public record CartItemResponse(
    Long id,
    String sweetName,
    double price,
    int quantity,
    double totalPrice
) {}