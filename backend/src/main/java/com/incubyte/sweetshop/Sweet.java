package com.incubyte.sweetshop;

import jakarta.persistence.*;

@Entity
@Table(name = "sweets")
public class Sweet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "price_cents", nullable = false)
    private Long price;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(nullable = false)
    private String category;

    public Sweet() {}

    public Sweet(Long id, String name, Long price, Integer quantity, String imageUrl, String category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public Long getPrice() { return price; }
    public Integer getQuantity() { return quantity; }
    public String getImageUrl() { return imageUrl; }
    public String getCategory() { return category; }

    public void setName(String name) { this.name = name; }
    public void setPrice(Long price) { this.price = price; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setCategory(String category) { this.category = category; }
}