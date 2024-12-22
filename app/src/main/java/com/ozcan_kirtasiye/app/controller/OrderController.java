package com.ozcan_kirtasiye.app.controller;

import com.ozcan_kirtasiye.app.dto.OrderDTO;
import com.ozcan_kirtasiye.app.model.Order;
import com.ozcan_kirtasiye.app.model.User;
import com.ozcan_kirtasiye.app.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/placeOrder")
    public ResponseEntity<OrderDTO> placeOrder(@AuthenticationPrincipal User user) {
        Order order = orderService.placeOrder(user);
        return ResponseEntity.ok(OrderDTO.from(order));
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getUserOrders(@AuthenticationPrincipal User user) {
        List<Order> orders = orderService.getOrdersByUser(user);
        return ResponseEntity.ok(orders.stream().map(OrderDTO::from).collect(Collectors.toList()));
    }
}

