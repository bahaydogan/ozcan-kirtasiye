package com.ozcan_kirtasiye.app.repository;

import com.ozcan_kirtasiye.app.model.Cart;
import com.ozcan_kirtasiye.app.model.CartItem;
import com.ozcan_kirtasiye.app.model.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICartItemRepo extends JpaRepository<CartItem, Long>
{
    void deleteByCartAndProduct(Cart cart, Product product);

    // Find cart items by product ID
    List<CartItem> findByProductId(Long productId);

    // Delete cart items by product ID
    @Modifying
    @Transactional
    void deleteByProductId(Long productId);


}
