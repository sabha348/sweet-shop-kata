package com.incubyte.sweetshop;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "users") // "user" is a reserved keyword in Postgres, so we use "users"
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Email(message = "must be a well-formed email address")
    @Column(unique = true)
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String role;
    // Default Constructor (Required by JPA)
    public User() {}

    // Constructor for our tests
    public User(String email, String password,String role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Getters
    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
}