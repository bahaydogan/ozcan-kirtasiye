package com.ozcan_kirtasiye.app.repository;

import com.ozcan_kirtasiye.app.model.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface IProductRepo extends JpaRepository<Product, Long> {
    Product findByName(String name);
    Product findByPrice(Double price);
    Boolean existsByName(String name);
    Product findById(long id);
    Page<Product> findAll(Pageable pageable); // Enable pagination


    List<Product> findByCategoryId(Long categoryId);
}
