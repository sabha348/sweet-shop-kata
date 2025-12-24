package com.incubyte.sweetshop;

import jakarta.persistence.*;

@Entity
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "sweet_id")
    private Sweet sweet;

    private Integer quantity;
    private Double priceAtPurchase;

    public OrderItem() {}

    public OrderItem(Sweet sweet, Integer quantity, Double priceAtPurchase) {
        this.sweet = sweet;
        this.quantity = quantity;
        this.priceAtPurchase = priceAtPurchase;
    }

    public void setOrder(Order order) { this.order = order; }
    public Sweet getSweet() { return sweet; }
    public Integer getQuantity() { return quantity; }
    public Double getPriceAtPurchase() { return priceAtPurchase; }
}