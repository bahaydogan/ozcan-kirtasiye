package com.ozcan_kirtasiye.app.repository;

import com.ozcan_kirtasiye.app.model.Product;
import com.ozcan_kirtasiye.app.model.ProductSummaryProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductSummaryRepository extends JpaRepository<Product, Long> {

    @Query(value = "SELECT * FROM product_summary", nativeQuery = true)
    List<ProductSummaryProjection> findAllProductSummaries();
}

