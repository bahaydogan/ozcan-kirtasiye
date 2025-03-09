package com.ozcan_kirtasiye.app.repository;

import com.ozcan_kirtasiye.app.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ICategoryRepo extends JpaRepository<Category, Long> {
    @Query("SELECT COUNT(c) > 0 FROM Category c WHERE LOWER(c.name) = LOWER(:name)")
    boolean existsByNameIgnoreCase(@Param("name") String name);

    Optional<Category> findByName(String name);
    Optional<Category> findById(Long id);
}
