package com.ozcan_kirtasiye.app.service;

import com.ozcan_kirtasiye.app.model.Product;
import com.ozcan_kirtasiye.app.repository.IProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductService implements IProductService {

    @Autowired
    private IProductRepo productRepo;

    @Override
    public Product saveProduct(Product product) {
        product.setCreateTime(LocalDateTime.now());
        return productRepo.save(product);
    }

    @Override
    public void deleteProduct(Long productId) {
        productRepo.deleteById(productId);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }


    public Product getProductById(Long productId) {
        return productRepo.findById(productId).get();
    }

    public Product getProductByName(String name) {
        return productRepo.findByName(name);
    }

    public Product getProductByPrice(Double price) {
        return productRepo.findByPrice(price);
    }

}
