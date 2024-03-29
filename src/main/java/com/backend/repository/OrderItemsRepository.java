package com.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.model.OrderItemsEntity;

public interface OrderItemsRepository extends JpaRepository<OrderItemsEntity, Integer> {

}
