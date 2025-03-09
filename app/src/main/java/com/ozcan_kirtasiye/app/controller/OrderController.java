package com.ozcan_kirtasiye.app.controller;

import com.ozcan_kirtasiye.app.dto.OrderDTO;
import com.ozcan_kirtasiye.app.dto.UserStatisticsDto;
import com.ozcan_kirtasiye.app.enums.OrderStatus;
import com.ozcan_kirtasiye.app.model.Order;
import com.ozcan_kirtasiye.app.model.User;
import com.ozcan_kirtasiye.app.security.CurrentUser;
import com.ozcan_kirtasiye.app.service.ITokenService;
import com.ozcan_kirtasiye.app.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;


    @GetMapping("/get")
    public ResponseEntity<List<OrderDTO>> getUserOrders(@AuthenticationPrincipal CurrentUser currentUser) {
        List<Order> orders = orderService.getOrdersByUserId(currentUser.getId());
        return ResponseEntity.ok(orders.stream().map(OrderDTO::from).collect(Collectors.toList()));
    }



    @GetMapping("/get-all-orders")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<Order> getAllOrders(
            @RequestParam(defaultValue = "0") int page,  // Default to page 0
            @RequestParam(defaultValue = "2") int size  // Default to 5 records per page
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return orderService.getAllOrders(pageable);
    }


    @PutMapping("/cancel")
    public String cancelOrder(@AuthenticationPrincipal CurrentUser currentUser, @RequestParam Long orderId) {

        if (orderService.getOrdersByUserId(currentUser.getId()).contains(orderService.getOrderById(orderId))) {
            try {
                orderService.cancelOrder(orderId);
                return "Order " + orderId + " has been canceled successfully.";
            } catch (RuntimeException e) {
                return e.getMessage();  // Return the error message if cancellation fails
            }
        }
        else return "not authorized for this order";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/cancelByAdmin")
    public ResponseEntity<?> cancelOrder2(@RequestParam Long orderId) {
        orderService.cancelOrder(orderId);
        return new ResponseEntity<>("order: "+ orderId+ "is cancelled by admin.",HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/status/{status}")
    public List<Order> getOrdersByStatus(@PathVariable OrderStatus status) {
        return orderService.getOrdersByStatus(status);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/date-range")
    public List<Order> getOrdersByDateRange(
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate) {

        // Convert the date strings to LocalDateTime
        LocalDateTime start = LocalDateTime.parse(startDate);
        LocalDateTime end = LocalDateTime.parse(endDate);

        return orderService.getOrdersByDateRange(start, end);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    //stored procedure
    @GetMapping("/user-statistics/{userId}")
    public ResponseEntity<UserStatisticsDto> getUserStatistics(@PathVariable Long userId) {
        UserStatisticsDto stats = orderService.getUserStatistics(userId);
        return ResponseEntity.ok(stats);
    }





}

