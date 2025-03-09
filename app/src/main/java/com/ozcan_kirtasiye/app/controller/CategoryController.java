package com.ozcan_kirtasiye.app.controller;

import com.ozcan_kirtasiye.app.dto.CategoryDTO;
import com.ozcan_kirtasiye.app.model.Category;
import com.ozcan_kirtasiye.app.model.Product;
import com.ozcan_kirtasiye.app.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody CategoryDTO categoryDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(categoryDTO));
    }

    @PostMapping("/{categoryId}/assign-product/{productId}")
    public ResponseEntity<Product> assignCategoryToProduct(
            @PathVariable Long categoryId,
            @PathVariable Long productId) {
        Product updatedProduct = categoryService.assignCategoryToProduct(productId, categoryId);
        return ResponseEntity.ok(updatedProduct);
    }

    // Get all products of a specific category
    @GetMapping("/{categoryId}/products")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable Long categoryId) {
        List<Product> products = categoryService.getProductsByCategory(categoryId);
        return ResponseEntity.ok(products);
    }

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO) {
        return ResponseEntity.ok(categoryService.updateCategory(id, categoryDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{categoryId}/addProduct")
    public ResponseEntity<?> addProductToCategory(
            @PathVariable Long categoryId,
            @RequestBody Product product) {
        Category updatedCategory = categoryService.addProductToCategory(categoryId, product).getCategory();
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }




}
