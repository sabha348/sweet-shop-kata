package com.incubyte.sweetshop;

public record OrderItemResponse(
    String sweetName,
    int quantity,
    double priceAtPurchase
) {}