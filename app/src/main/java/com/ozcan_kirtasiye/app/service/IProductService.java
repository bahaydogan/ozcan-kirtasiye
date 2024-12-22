package com.ozcan_kirtasiye.app.service;

import com.ozcan_kirtasiye.app.model.Product;

import java.util.List;

public interface IProductService {
    Product saveProduct(Product product);

    void deleteProduct(Long productId);

    List<Product> getAllProducts();

    Product getProductById(Long productId);
}
