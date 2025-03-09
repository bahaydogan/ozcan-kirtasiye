package com.ozcan_kirtasiye.app.service;

import com.ozcan_kirtasiye.app.model.Category;
import com.ozcan_kirtasiye.app.model.Product;
import com.ozcan_kirtasiye.app.model.ProductSummaryProjection;
import com.ozcan_kirtasiye.app.repository.ICartItemRepo;
import com.ozcan_kirtasiye.app.repository.IProductRepo;
import com.ozcan_kirtasiye.app.repository.ProductSummaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService implements IProductService {

    @Autowired
    private IProductRepo productRepo;

    @Autowired
    private ICartItemRepo cartItemRepo;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductSummaryRepository productSummaryRepository;

    @Autowired
    private FileStorageService fileStorageService;


    @Override
    public Product saveProduct(Product product) {
        // Check if the product already exists by name
        Optional<Product> existingProduct = Optional.ofNullable(productRepo.findByName(product.getName()));

        if (existingProduct.isPresent() && product.getStockQuantity() != null) {
            long existingProductId = existingProduct.get().getId();
            System.out.printf("\n\nThere exists a product. UPDATING product with ID: %d\n", existingProductId);
            return updateProductById(existingProductId, product);
        }

        // Handle category assignment
        if (product.getCategory() != null) {
            Category category = resolveCategory(product.getCategory());
            product.setCategory(category);
        }

        // Set default values for new product
        if (product.getStockQuantity() == null) {
            product.setStockQuantity(1);
        }
        product.setCreateTime(LocalDateTime.now());

        // Save and return the new product
        return productRepo.save(product);
    }

    private Category resolveCategory(Category category) {
        if (category.getId() != null) {
            return (Category) categoryService.findById(category.getId())
                    .orElseThrow(() -> new RuntimeException("Category not found with ID: " + category.getId()));
        } else if (category.getName() != null) {
            return (Category) categoryService.findByName(category.getName())
                    .orElseThrow(() -> new RuntimeException("Category not found with name: " + category.getName()));
        } else {
            throw new RuntimeException("Category must have either an ID or a name.");
        }
    }



    @Override
    public void deleteProduct(Long productId) {
        //in case the product exists in someone's cart

        // Delete cart items for this product
        cartItemRepo.deleteByProductId(productId);

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

    @Override
    public Product updateProductById(Long id, Product product) {
        Product existingProduct = productRepo.findById(id).orElse(null);
        if (existingProduct != null) {
            // Update fields of the existing product
            if (product.getPrice()!=null)existingProduct.setPrice(product.getPrice());
            if (product.getStockQuantity()!=null) existingProduct.setStockQuantity(product.getStockQuantity());
            if (product.getDescription()!=null) existingProduct.setDescription(product.getDescription());
            if (product.getCategory()!=null) existingProduct.setCategory(resolveCategory(product.getCategory()));

            return productRepo.save(existingProduct); // Save the updated product
        } else {
            System.out.println("Product with ID " + id + " does not exist.");
            return null; // Product not found, return null
        }
    }


    @Override
    public Product updateProductStock(String name, int newStockQuantity) {
        // Ürünü adı ile bul
        Product existingProduct = productRepo.findByName(name);

        if (existingProduct != null) {

            if (newStockQuantity < 0) {

                throw new IllegalArgumentException("Stok miktarı negatif olamaz.");
            }


            existingProduct.setStockQuantity(newStockQuantity);


            return productRepo.save(existingProduct);
        } else {

            return null;
        }
    }

    @Override
    //view
    public List<ProductSummaryProjection> getProductSummaries() {
        return productSummaryRepository.findAllProductSummaries();
    }

    public Product findById(Long id) {
        return productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));
    }

    public String uploadProductImage(Long productId, MultipartFile file) throws IOException {
        // Fetch the product
        Product product = findById(productId);

        // Store the file and get the file URL
        String imageUrl = fileStorageService.storeFile(file);

        // Update the product with the new image URL
        product.setImageUrl(imageUrl);
        productRepo.save(product);

        return imageUrl;
    }


    public Resource getProductImage(Long productId) throws IOException {
        Product product = findById(productId);

        if (product.getImageUrl() == null) {
            throw new RuntimeException("No image associated with product ID: " + productId);
        }

        // Get the file path from the stored image URL
        String fileName = product.getImageUrl().substring(product.getImageUrl().lastIndexOf("/") + 1);
        return fileStorageService.loadFileAsResource(fileName);
    }

    public void deleteProductImage(Long productId) throws IOException {
        Product product = findById(productId);

        if (product.getImageUrl() != null) {
            // Extract file name from URL
            String fileName = product.getImageUrl().substring(product.getImageUrl().lastIndexOf("/") + 1);

            // Delete the file
            fileStorageService.deleteFile(fileName);

            // Remove the image URL from the product
            product.setImageUrl(null);
            productRepo.save(product);
        } else {
            throw new RuntimeException("No image associated with product ID: " + productId);
        }
    }

    public String updateProductImage(Long productId, MultipartFile file) throws IOException {
        // Delete the existing image
        deleteProductImage(productId);

        // Upload the new image
        return uploadProductImage(productId, file);
    }

}
