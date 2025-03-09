package com.ozcan_kirtasiye.app.repository;

import com.ozcan_kirtasiye.app.enums.OrderStatus;
import com.ozcan_kirtasiye.app.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public interface IOrderRepo extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
    List<Order> findByStatus(OrderStatus status);
    List<Order> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    @Override
    Page<Order> findAll(Pageable pageable);

    @Query(value = "CALL calculate_user_statistics(:userId)", nativeQuery = true)
    void calculateUserStatistics(@Param("userId") Long userId);



}
