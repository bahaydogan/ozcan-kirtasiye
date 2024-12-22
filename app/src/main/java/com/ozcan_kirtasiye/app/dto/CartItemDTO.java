package com.ozcan_kirtasiye.app.dto;

import com.ozcan_kirtasiye.app.model.CartItem;
import lombok.Data;

// If needed to convert back from DTO to entity (rare), you could do:
// public CartItem toEntity(Cart cart, Product product) { ... }

// getters, setters

@Data
public class CartItemDTO {
    private Long productId;
    private String productName;
    private Integer quantity;
    private Double unitPrice;
    private Double totalPrice;

    public static CartItemDTO from(CartItem cartItem) {
        CartItemDTO dto = new CartItemDTO();
        dto.setProductId(cartItem.getProduct().getId());
        dto.setProductName(cartItem.getProduct().getName());
        dto.setQuantity(cartItem.getQuantity());
        Double price = cartItem.getProduct().getPrice();
        dto.setUnitPrice(price);
        dto.setTotalPrice(price * cartItem.getQuantity());
        return dto;
    }
    // getters, setters
}


