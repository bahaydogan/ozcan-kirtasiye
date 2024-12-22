package com.ozcan_kirtasiye.app.controller;

import com.ozcan_kirtasiye.app.dto.CartDTO;
import com.ozcan_kirtasiye.app.model.Cart;
import com.ozcan_kirtasiye.app.model.User;
import com.ozcan_kirtasiye.app.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public ResponseEntity<CartDTO> getCart(@AuthenticationPrincipal User user) {
        Cart cart = cartService.getOrCreateCart(user);
        return ResponseEntity.ok(CartDTO.from(cart));
    }

    @PostMapping("/add")
    public ResponseEntity<CartDTO> addItemToCart(@AuthenticationPrincipal User user,
                                                 @RequestParam Long productId,
                                                 @RequestParam int quantity) {
        Cart updatedCart = cartService.addItemToCart(user, productId, quantity);
        return ResponseEntity.ok(CartDTO.from(updatedCart));
    }

    // More endpoints like remove item, update quantity, etc.
}

