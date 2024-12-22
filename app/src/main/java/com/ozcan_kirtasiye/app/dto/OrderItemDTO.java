package com.ozcan_kirtasiye.app.dto;

import com.ozcan_kirtasiye.app.model.OrderItem;
import lombok.Data;

@Data
public class OrderItemDTO {
    private Long productId;
    private String productName;
    private Integer quantity;
    private Double unitPrice;
    private Double totalPrice;

    public static OrderItemDTO from(OrderItem orderItem) {
        OrderItemDTO dto = new OrderItemDTO();
        dto.setProductId(orderItem.getProduct().getId());
        dto.setProductName(orderItem.getProduct().getName());
        dto.setQuantity(orderItem.getQuantity());
        dto.setUnitPrice(orderItem.getUnitPrice());
        dto.setTotalPrice(orderItem.getUnitPrice() * orderItem.getQuantity());
        return dto;
    }
    // getters, setters
}


