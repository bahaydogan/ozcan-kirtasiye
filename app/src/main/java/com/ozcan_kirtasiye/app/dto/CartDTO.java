package com.ozcan_kirtasiye.app.dto;

import com.ozcan_kirtasiye.app.model.Cart;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class CartDTO {
    private Long id;
    private List<CartItemDTO> items;
    private Double totalCartPrice;

    public static CartDTO from(Cart cart) {
        CartDTO dto = new CartDTO();
        dto.setId(cart.getId());
        List<CartItemDTO> itemDTOs = cart.getItems().stream()
                .map(CartItemDTO::from)
                .collect(Collectors.toList());
        dto.setItems(itemDTOs);

        Double total = itemDTOs.stream()
                .mapToDouble(CartItemDTO::getTotalPrice)
                .sum();
        dto.setTotalCartPrice(total);

        return dto;
    }

}





