package com.ozcan_kirtasiye.app.service;

import com.ozcan_kirtasiye.app.model.*;
import com.ozcan_kirtasiye.app.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private IOrderRepo orderRepository;

    @Autowired
    private IOrderItemRepo orderItemRepository;

    @Autowired
    private IProductRepo productRepository;

    @Autowired
    private CartService cartService;

    @Transactional
    public Order placeOrder(User user) {
        Cart cart = cartService.getOrCreateCart(user);

        // Create Order
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());

        double totalPrice = 0.0;

        // Convert each CartItem -> OrderItem
        for (CartItem cartItem : cart.getItems()) {
            Product product = cartItem.getProduct();

            // Check stock again just to be safe
            if (product.getStockQuantity() < cartItem.getQuantity()) {
                System.out.println("Not enough stock for product: " + product.getName());
            }

            product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());
            productRepository.save(product);

            double itemTotal = product.getPrice() * cartItem.getQuantity();
            totalPrice += itemTotal;

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setUnitPrice(product.getPrice());

            order.getOrderItems().add(orderItem);
        }

        order.setTotalPrice(totalPrice);
        orderRepository.save(order);

        // Optionally clear cart
        cart.getItems().clear();
        // Or just remove the cart entirely, your choice

        return order;
    }

    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUserId(user.getId());
    }

    // Admin can get all orders
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}

