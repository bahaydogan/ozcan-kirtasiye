package com.ozcan_kirtasiye.app.dto;

import com.ozcan_kirtasiye.app.model.Order;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class OrderDTO {
    private Long id;
    private LocalDateTime orderDate;
    private Double totalPrice;
    private List<OrderItemDTO> orderItems;

    public static OrderDTO from(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setOrderDate(order.getOrderDate());
        dto.setTotalPrice(order.getTotalPrice());
        List<OrderItemDTO> itemDTOs = order.getOrderItems().stream()
                .map(OrderItemDTO::from)
                .collect(Collectors.toList());
        dto.setOrderItems(itemDTOs);
        return dto;
    }
    // getters, setters
}



