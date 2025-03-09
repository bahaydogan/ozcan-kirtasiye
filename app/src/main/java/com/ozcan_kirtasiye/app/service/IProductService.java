package com.ozcan_kirtasiye.app.service;

import com.ozcan_kirtasiye.app.model.Product;
import com.ozcan_kirtasiye.app.model.ProductSummaryProjection;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IProductService {
    Product saveProduct(Product product);

    void deleteProduct(Long productId);

    List<Product> getAllProducts();

    Product getProductById(Long productId);

    Product updateProductById(Long id, Product product);

    Product updateProductStock(String productName, int quantityChange);

    List<ProductSummaryProjection> getProductSummaries();

    String uploadProductImage(Long productId, MultipartFile file) throws IOException;

    Resource getProductImage(Long productId) throws IOException;

    void deleteProductImage(Long productId) throws IOException;
    String updateProductImage(Long productId, MultipartFile file) throws IOException;


}
