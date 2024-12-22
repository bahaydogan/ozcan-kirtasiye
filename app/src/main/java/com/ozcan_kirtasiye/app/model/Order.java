package com.ozcan_kirtasiye.app.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime orderDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Could store final cost at the time of order
    private Double totalPrice;

    // Typically you’d store an enum status: PLACED, CANCELLED, SHIPPED, etc.
    // Not strictly required if you're not implementing shipping for now
    // private OrderStatus status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    // Constructors, getters, setters
}

