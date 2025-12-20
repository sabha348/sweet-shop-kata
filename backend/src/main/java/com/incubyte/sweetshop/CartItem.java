package com.incubyte.sweetshop;

import jakarta.persistence.*;

@Entity
@Table(name = "cart_items")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "sweet_id")
    private Sweet sweet;

    private Integer quantity;

    public CartItem() {}
    public CartItem(Cart cart, Sweet sweet, Integer quantity) {
        this.cart = cart;
        this.sweet = sweet;
        this.quantity = quantity;
    }

    // Getters and Setters
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public Integer getQuantity() { return quantity; }
    public Sweet getSweet() { return sweet; }
}