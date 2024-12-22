package com.ozcan_kirtasiye.app.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
@Data
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // We assume one cart belongs to one user
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    // The total cost of the cart can be computed dynamically or stored
    // For simplicity, we'll just store items and compute cost on the fly

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();


}

