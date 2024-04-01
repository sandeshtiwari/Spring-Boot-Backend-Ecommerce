package com.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.backend.dto.CategoryImageDto;
import com.backend.model.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {
    @Query("SELECT new com.backend.dto.CategoryImageDto(p.category, MIN(p.image)) FROM ProductEntity p GROUP BY p.category")
    List<CategoryImageDto> findDistinctCategoriesWithImages();

    List<ProductEntity> findByCategory(String category);

}
