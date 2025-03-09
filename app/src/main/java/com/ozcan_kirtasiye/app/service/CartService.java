package com.ozcan_kirtasiye.app.service;

import com.ozcan_kirtasiye.app.dto.CartItemDTO;
import com.ozcan_kirtasiye.app.model.*;
import com.ozcan_kirtasiye.app.repository.ICartItemRepo;
import com.ozcan_kirtasiye.app.repository.ICartRepo;
import com.ozcan_kirtasiye.app.repository.IOrderRepo;
import com.ozcan_kirtasiye.app.repository.IProductRepo;
import com.ozcan_kirtasiye.app.security.CurrentUser;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class CartService {

    @Autowired
    private ICartRepo cartRepository;

    @Autowired
    private ICartItemRepo cartItemRepository;

    @Autowired
    private IProductRepo productRepository;

    @Autowired
    private IOrderRepo orderRepository;

    @Autowired
    private IUserService userService;


    public Cart getOrCreateCart(CurrentUser currentUser) {
        User user = userService.getUserById(currentUser.getId());
        return cartRepository.findByUserId(currentUser.getId())
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });
    }

    public Cart addItemToCart(CurrentUser currentUser, Long productId, int quantity) {
        Cart cart = getOrCreateCart(currentUser);
        Product product = productRepository.findById(productId)
                .orElseThrow();

        // Check stock if needed
        if (product.getStockQuantity() < quantity) {
            System.out.println("Not enough stock. left: " + product.getStockQuantity());
            return null;
        }

        // Check if item already in cart
        Optional<CartItem> existingItemOpt = cart.getItems().stream()
                .filter(ci -> ci.getProduct().getId() == productId)
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

    public Cart removeItemFromCart(CurrentUser currentUser, Long productId) {
        Cart cart = getOrCreateCart(currentUser);
        cart.getItems().removeIf(item -> item.getProduct().getId() == (productId));
        cartRepository.save(cart);
        cartItemRepository.deleteByCartAndProduct(cart, productRepository.findById(productId).get());
        return cart;
    }

    public Cart updateCartItemQuantity(CurrentUser currentUser, Long productId, int quantity) {
        Cart cart = getOrCreateCart(currentUser);
        Optional<CartItem> itemOpt = cart.getItems().stream()
                .filter(ci -> ci.getProduct().getId() == (productId))
                .findFirst();

        if (itemOpt.isPresent()) {
            CartItem item = itemOpt.get();
            if (quantity <= 0) {
                cart.getItems().remove(item);
                cartItemRepository.delete(item);
            } else {
                item.setQuantity(quantity);
                cartItemRepository.save(item);
            }
        }
        return cartRepository.save(cart);
    }

    public Cart clearCart(CurrentUser currentUser) {
        Cart cart = getOrCreateCart(currentUser);
        cartItemRepository.deleteAll(cart.getItems());
        cart.getItems().clear();
        return cartRepository.save(cart);
    }

    public void deleteCartFromDB(CurrentUser currentUser) {

        // Retrieve the user's cart
        Cart cart = getOrCreateCart(currentUser);

        // If the cart is not empty, clear its items
        if (!cart.getItems().isEmpty()) {
            clearCart(currentUser);
        }

        // Delete the cart from the database
        cartRepository.delete(cart);
    }


    public double getCartTotal(CurrentUser currentUser) {
        Cart cart = getOrCreateCart(currentUser);
        return cart.getItems().stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
    }

    public List<CartItemDTO> listCartItems(CurrentUser currentUser) {
        Cart cart = getOrCreateCart(currentUser);
        return cart.getItems().stream()
                .map(CartItemDTO::from)
                .collect(Collectors.toList());
    }


    @Transactional
    public void checkoutCart(CurrentUser currentUser) {
        User user = userService.getUserById(currentUser.getId());
        Cart cart = getOrCreateCart(currentUser);

        // Create Order
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());

        double totalPrice = 0.0;

        for (CartItem cartItem : cart.getItems()) {
            Product product = cartItem.getProduct();

            // Stock validation
            if (product.getStockQuantity() < cartItem.getQuantity()) {
                throw new RuntimeException("error: left " + product.getStockQuantity() + " stock for " + product.getName());
            }

            // Update stock
            product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());
            productRepository.save(product);

            // Calculate total and create OrderItem
            double itemTotal = product.getPrice() * cartItem.getQuantity();
            totalPrice += itemTotal;

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setUnitPrice(product.getPrice());

            order.getOrderItems().add(orderItem);
        }

        order.setTotalPrice(totalPrice);
        orderRepository.save(order);

        clearCart(currentUser);
        deleteCartFromDB(currentUser);
    }


    public Cart removeItemByQuantity(CurrentUser currentUser, Long productId, int quantity) {
        // Get or create the user's cart
        Cart cart = getOrCreateCart(currentUser);

        // Find the product in the cart
        CartItem cartItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId() == (productId))
                .findFirst()
                .orElseThrow(null);

        // Decrease the quantity or remove the item completely if quantity <= 0
        if (cartItem.getQuantity() > quantity) {
            cartItem.setQuantity(cartItem.getQuantity() - quantity);
        } else {
            cart.getItems().remove(cartItem);
        }

        // Save the updated cart
        cartRepository.save(cart);

        return cart;
    }


}

