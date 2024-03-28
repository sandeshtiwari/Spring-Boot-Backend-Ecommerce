package com.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.model.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {

}
