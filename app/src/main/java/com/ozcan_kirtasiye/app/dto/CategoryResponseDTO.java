package com.ozcan_kirtasiye.app.dto;

import com.ozcan_kirtasiye.app.model.Product;

import java.util.List;

public class CategoryResponseDTO {
    private Long id;
    private String name;
    private String description;
    private List<Product> products; // Halihazırda tanımlı bir DTO
}
