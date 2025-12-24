package com.incubyte.sweetshop;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private LocalDateTime orderDate;
    private Double totalPrice;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items = new ArrayList<>();

    public Order() {}

    public Order(User user, LocalDateTime orderDate, Double totalPrice) {
        this.user = user;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
    }

    // Getters and helper to add items
    public Long getId() { return id; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public Double getTotalPrice() { return totalPrice; }
    public List<OrderItem> getItems() { return items; }
    
    public void addOrderItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }
}