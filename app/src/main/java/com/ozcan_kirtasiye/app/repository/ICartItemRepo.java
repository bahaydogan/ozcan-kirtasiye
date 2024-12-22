package com.ozcan_kirtasiye.app.repository;

import com.ozcan_kirtasiye.app.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICartItemRepo extends JpaRepository<CartItem, Long>
{ }
