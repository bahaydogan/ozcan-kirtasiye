package com.ozcan_kirtasiye.app.model;

import java.time.LocalDateTime;

public interface ProductSummaryProjection {
    Long getProductId();
    String getProductName();
    Double getPrice();
    Integer getStockQuantity();
    LocalDateTime getCreateTime();
}

