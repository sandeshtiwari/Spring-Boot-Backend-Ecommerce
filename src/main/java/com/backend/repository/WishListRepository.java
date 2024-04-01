package com.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.model.ProductEntity;
import com.backend.model.UserEntity;
import com.backend.model.WishListEntity;

public interface WishListRepository extends JpaRepository<WishListEntity, Integer> {
    boolean existsByUserAndProduct(UserEntity user, ProductEntity product);

    Optional<WishListEntity> findByUserAndProduct(UserEntity user, ProductEntity product);
}
