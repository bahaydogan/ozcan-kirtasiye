package com.ozcan_kirtasiye.app.service;

import com.ozcan_kirtasiye.app.model.Cart;
import com.ozcan_kirtasiye.app.model.CartItem;
import com.ozcan_kirtasiye.app.model.Product;
import com.ozcan_kirtasiye.app.repository.ICartItemRepo;
import com.ozcan_kirtasiye.app.repository.ICartRepo;
import com.ozcan_kirtasiye.app.repository.IProductRepo;
import org.springframework.stereotype.Service;
import com.ozcan_kirtasiye.app.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Optional;


@Service
public class CartService {

    @Autowired
    private ICartRepo cartRepository;

    @Autowired
    private ICartItemRepo cartItemRepository;

    @Autowired
    private IProductRepo productRepository;

    public Cart getOrCreateCart(User user) {
        return cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });
    }

    public Cart addItemToCart(User user, Long productId, int quantity) {
        Cart cart = getOrCreateCart(user);
        Product product = productRepository.findById(productId)
                .orElseThrow();

        // Check stock if needed
        if (product.getStockQuantity() < quantity) {
            System.out.println("Not enough stock");
        }

        // Check if item already in cart
        Optional<CartItem> existingItemOpt = cart.getItems().stream()
                .filter(ci -> ci.getProduct().getId() == productId) // use == for primitive comparison
                .findFirst();

        if (existingItemOpt.isPresent()) {
            CartItem existingItem = existingItemOpt.get();
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            cartItemRepository.save(existingItem);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            cart.getItems().add(newItem);
            cartItemRepository.save(newItem);
        }

        return cartRepository.save(cart);
    }

    // More methods: removeItemFromCart, updateCartItemQuantity, clearCart, etc.
}

