package com.ozcan_kirtasiye.app.controller;

import com.ozcan_kirtasiye.app.dto.CartDTO;
import com.ozcan_kirtasiye.app.model.Cart;
import com.ozcan_kirtasiye.app.model.User;
import com.ozcan_kirtasiye.app.security.CurrentUser;
import com.ozcan_kirtasiye.app.service.CartService;
import com.ozcan_kirtasiye.app.service.ITokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public ResponseEntity<CartDTO> getCart(@AuthenticationPrincipal CurrentUser currentUser) {

        Cart cart = cartService.getOrCreateCart(currentUser);
        return ResponseEntity.ok(CartDTO.from(cart));
    }

    @GetMapping("/list")
    public ResponseEntity<?> getCartList(@AuthenticationPrincipal CurrentUser currentUser) {

        return new ResponseEntity<>(cartService.listCartItems(currentUser), HttpStatus.OK);
    }


    @PostMapping("/add")
    public ResponseEntity<CartDTO> addItemToCart(@AuthenticationPrincipal CurrentUser currentUser,
                                                 @RequestParam Long productId,
                                                 @RequestParam int quantity) {

        Cart updatedCart = cartService.addItemToCart(currentUser, productId, quantity);
        return ResponseEntity.ok(CartDTO.from(updatedCart));
    }

    @DeleteMapping("/remove")
    public ResponseEntity<CartDTO> removeItemFromCart(@AuthenticationPrincipal CurrentUser currentUser,
                                                      @RequestParam Long productId) {
        Cart updatedCart = cartService.removeItemFromCart(currentUser, productId);
        return ResponseEntity.ok(CartDTO.from(updatedCart));
    }

    @DeleteMapping("/remove-by-quantity")
    public ResponseEntity<CartDTO> removeItemByQuantity(@AuthenticationPrincipal CurrentUser currentUser,
                                                        @RequestParam Long productId,
                                                        @RequestParam int quantity) {


        // Remove the product based on quantity
        Cart updatedCart = cartService.removeItemByQuantity(currentUser, productId, quantity);

        // Return the updated cart as DTO
        return ResponseEntity.ok(CartDTO.from(updatedCart));
    }


    @PutMapping("/update")
    public ResponseEntity<CartDTO> updateCartItemQuantity(@AuthenticationPrincipal CurrentUser currentUser,
                                                          @RequestParam Long productId,
                                                          @RequestParam int quantity) {
        Cart updatedCart = cartService.updateCartItemQuantity(currentUser, productId, quantity);
        return ResponseEntity.ok(CartDTO.from(updatedCart));
    }

    @DeleteMapping("/clear")
    public ResponseEntity<CartDTO> clearCart(@AuthenticationPrincipal CurrentUser currentUser) {
        Cart updatedCart = cartService.clearCart(currentUser);
        return ResponseEntity.ok(CartDTO.from(updatedCart));
    }

    @DeleteMapping("/deleteCart")
    public ResponseEntity<String> deleteCart(@AuthenticationPrincipal CurrentUser currentUser) {

        // Call the service method to delete the cart from the database
        cartService.deleteCartFromDB(currentUser);

        // Return a response indicating success
        return ResponseEntity.ok("Cart deleted successfully.");
    }

    @GetMapping("/total")
    public ResponseEntity<Double> getCartTotal(@AuthenticationPrincipal CurrentUser currentUser) {
;
        double total = cartService.getCartTotal(currentUser);
        return ResponseEntity.ok(total);
    }

    @PostMapping("/checkout")
    public ResponseEntity<String> checkoutCart(@AuthenticationPrincipal CurrentUser currentUser) {
        cartService.checkoutCart(currentUser);
        return ResponseEntity.ok("Checkout successful.");
    }

}

