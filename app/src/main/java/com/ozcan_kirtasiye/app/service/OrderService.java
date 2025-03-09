package com.ozcan_kirtasiye.app.service;

import com.ozcan_kirtasiye.app.dto.UserStatisticsDto;
import com.ozcan_kirtasiye.app.model.*;
import com.ozcan_kirtasiye.app.repository.*;
import com.ozcan_kirtasiye.app.enums.OrderStatus;
import com.ozcan_kirtasiye.app.security.CurrentUser;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private IOrderRepo orderRepository;

    @Autowired
    private IProductRepo productRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public List<Order> getOrdersByUserId(long userId) {
        return orderRepository.findByUserId(userId);
    }


    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    // Admin can get all orders
    public Page<Order> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    public List<Order> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    public List<Order> getOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findByOrderDateBetween(startDate, endDate);
    }


    @Transactional
    public void cancelOrder(Long orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();

            if (order.getStatus().equals(OrderStatus.IN_PROCESS)) {
                // Restock products
                for (OrderItem orderItem : order.getOrderItems()) {
                    Product product = orderItem.getProduct();
                    product.setStockQuantity(product.getStockQuantity() + orderItem.getQuantity());
                    productRepository.save(product);
                }

                // Mark the order as canceled
                order.setStatus(OrderStatus.CANCELLED);
                orderRepository.save(order);
            } else {
                throw new RuntimeException("Order cannot be canceled as it's already processed or shipped.");
            }
        } else {
            throw new RuntimeException("Order not found.");
        }
    }

    //stored procedure
    public UserStatisticsDto getUserStatistics(Long userId) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("calculate_user_statistics");

        // Register input and output parameters
        query.registerStoredProcedureParameter("input_user_id", Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("total_spent", BigDecimal.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("total_orders", Integer.class, ParameterMode.OUT);

        // Set input parameter
        query.setParameter("input_user_id", userId);

        // Execute the procedure
        query.execute();

        // Get output parameters
        BigDecimal totalSpent = (BigDecimal) query.getOutputParameterValue("total_spent");
        Integer totalOrders = (Integer) query.getOutputParameterValue("total_orders");

        return new UserStatisticsDto(userId, totalSpent, totalOrders);
    }



}

