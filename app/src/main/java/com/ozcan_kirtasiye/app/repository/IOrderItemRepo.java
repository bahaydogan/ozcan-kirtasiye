package com.ozcan_kirtasiye.app.repository;

import com.ozcan_kirtasiye.app.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IOrderItemRepo extends JpaRepository<OrderItem, Long>
{ }
