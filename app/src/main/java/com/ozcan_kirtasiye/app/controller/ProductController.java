package com.ozcan_kirtasiye.app.controller;

import com.ozcan_kirtasiye.app.model.Product;
import com.ozcan_kirtasiye.app.model.ProductSummaryProjection;
import com.ozcan_kirtasiye.app.service.FileStorageService;
import com.ozcan_kirtasiye.app.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("api/product")
public class ProductController {

    @Autowired
    private IProductService productService;



    @PostMapping
    public ResponseEntity<?> saveProduct(@RequestBody Product product) {
        return new ResponseEntity<>(productService.saveProduct(product), HttpStatus.CREATED);

    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAllProducts() {
        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
    }


    @GetMapping("/{productId}")
    public ResponseEntity<?> getProductById(@PathVariable Long productId) {
        return new ResponseEntity<>(productService.getProductById(productId), HttpStatus.OK);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<?> updateProductById(@PathVariable Long productId, @RequestBody Product product) {
        return new ResponseEntity<>(productService.updateProductById(productId,product), HttpStatus.OK);
    }

    @PutMapping("/update-stock/{productName}")
    public ResponseEntity<?> updateProductStock(@PathVariable String productName, @RequestBody Integer newStockQuantity) {
        try {
            Product updatedProduct = productService.updateProductStock(productName, newStockQuantity);
            if (updatedProduct != null) {
                return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Ürün bulunamadı.", HttpStatus.NOT_FOUND);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }



    @Transactional
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/{id}/upload-image")
    public ResponseEntity<String> uploadImage(@PathVariable Long id, @RequestParam("image") MultipartFile file) {
        try {
            // Delegate the upload logic to the service
            String imageUrl = productService.uploadProductImage(id, file);
            return ResponseEntity.ok("Image uploaded successfully: " + imageUrl);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error uploading image: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<Resource> getImage(@PathVariable Long id) {
        try {
            Resource image = productService.getProductImage(id);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) // Or IMAGE_PNG based on the file type
                    .body(image);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}/image")
    public ResponseEntity<String> deleteImage(@PathVariable Long id) {
        try {
            productService.deleteProductImage(id);
            return ResponseEntity.ok("Image deleted successfully for product ID: " + id);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error deleting image: " + e.getMessage());
        }
    }

    @Transactional
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}/image")
    public ResponseEntity<String> updateImage(@PathVariable Long id, @RequestParam("image") MultipartFile file) {
        try {
            String imageUrl = productService.updateProductImage(id, file);
            return ResponseEntity.ok("Image updated successfully: " + imageUrl);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error updating image: " + e.getMessage());
        }
    }

    //view
    @GetMapping("/view/summary")
    public ResponseEntity<List<ProductSummaryProjection>> getProductSummaries() {
        List<ProductSummaryProjection> summaries = productService.getProductSummaries();
        return ResponseEntity.ok(summaries);
    }


}
