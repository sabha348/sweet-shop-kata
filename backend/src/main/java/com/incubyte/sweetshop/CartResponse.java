package com.incubyte.sweetshop;

import java.util.List;

public record CartResponse(
    Long id,
    List<CartItemResponse> items,
    double grandTotal
) {}