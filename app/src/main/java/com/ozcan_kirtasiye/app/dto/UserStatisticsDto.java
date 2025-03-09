package com.ozcan_kirtasiye.app.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserStatisticsDto {
    private long userId;
    private BigDecimal totalSpent;
    private int totalOrders;

    // Constructor, getters, setters
    public UserStatisticsDto(long userId, BigDecimal totalSpent, int totalOrders) {
        this.userId = userId;
        this.totalSpent = totalSpent;
        this.totalOrders = totalOrders;
    }

    // Getters and setters omitted for brevity
}

