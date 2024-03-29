package com.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.model.OrderEntity;

public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {

}
