package com.ozcan_kirtasiye.app.repository;

import com.ozcan_kirtasiye.app.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IOrderRepo extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
}
