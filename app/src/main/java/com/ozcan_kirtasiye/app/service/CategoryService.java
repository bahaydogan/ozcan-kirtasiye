package com.ozcan_kirtasiye.app.service;

import com.ozcan_kirtasiye.app.dto.CategoryDTO;
import com.ozcan_kirtasiye.app.model.Category;
import com.ozcan_kirtasiye.app.model.Product;
import com.ozcan_kirtasiye.app.repository.ICategoryRepo;
import com.ozcan_kirtasiye.app.repository.IProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private ICategoryRepo categoryRepo;

    @Autowired
    private IProductRepo productRepository;

    public Category createCategory(CategoryDTO categoryDTO) {

        if (categoryRepo.existsByNameIgnoreCase(categoryDTO.getName())) {
            throw new IllegalArgumentException("Category already exists");
        }

        Category category = new Category();
        category.setName(categoryDTO.getName());
        return categoryRepo.save(category);
    }

    public List<Category> getAllCategories() {
        return categoryRepo.findAll();
    }

    public Category updateCategory(Long id, CategoryDTO categoryDTO) {
        Category category = categoryRepo.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
        category.setName(categoryDTO.getName());
        return categoryRepo.save(category);
    }

    public void deleteCategory(Long id) {
        categoryRepo.deleteById(id);
    }

    public Product addProductToCategory(Long categoryId, Product product) {
        // Kategorinin var olup olmadığını kontrol et
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // Ürünün veri tabanında mevcut olup olmadığını kontrol et
        Optional<Product> existingProductOptional = Optional.ofNullable(productRepository.findByName(product.getName()));

        Product productToSave;

        if (existingProductOptional.isPresent()) {
            // Mevcut ürün varsa
            productToSave = existingProductOptional.get();

            // Yeni fiyat ve stok bilgisi girilmişse mevcut bilgileri güncelle
            if (product.getPrice() != null) {
                if (product.getPrice().compareTo(0.0) < 0) {
                    throw new IllegalArgumentException("Price cannot be less than 0");
                }
                productToSave.setPrice(product.getPrice());
            }
            if (product.getStockQuantity() != null) {
                if (product.getStockQuantity() < 0) {
                    throw new IllegalArgumentException("Stock quantity cannot be less than 0");
                }
                productToSave.setStockQuantity(product.getStockQuantity());
            }
        } else {
            // Yeni ürün ekleniyorsa adı dışında stok ve fiyat bilgileri zorunlu
            if (product.getStockQuantity() == null || product.getPrice() == null) {
                throw new IllegalArgumentException("Please provide stock quantity and price information");
            }

            if (product.getPrice().compareTo(0.0) < 0) {
                throw new IllegalArgumentException("Price cannot be less than 0");
            }
            if (product.getStockQuantity() < 0) {
                throw new IllegalArgumentException("Stock quantity cannot be less than 0");
            }

            productToSave = product;
        }

        // Ürünü kategori ile ilişkilendir
        productToSave.setCategory(category);

        // Ürünü kaydet
        return productRepository.save(productToSave);
    }

    public Product assignCategoryToProduct(Long productId, Long categoryId) {
        // Fetch the product by ID
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

        // Fetch the category by ID
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + categoryId));

        // Assign the category to the product
        product.setCategory(category);

        // Save and return the updated product
        return productRepository.save(product);
    }


    // Fetch products by category
    public List<Product> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    public Optional<Category> findById(Long id) {
        return Optional.ofNullable(categoryRepo.findById(id).orElseThrow(() -> new RuntimeException("Category not found")));
    }

    public Optional<Category> findByName(String name) {
        return Optional.ofNullable(categoryRepo.findByName(name).orElseThrow(() -> new RuntimeException("Category not found")));
    }
}
