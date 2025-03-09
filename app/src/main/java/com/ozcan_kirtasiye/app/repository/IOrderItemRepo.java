package com.ozcan_kirtasiye.app.repository;

import com.ozcan_kirtasiye.app.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface IOrderItemRepo extends JpaRepository<OrderItem, Long>
{


}
