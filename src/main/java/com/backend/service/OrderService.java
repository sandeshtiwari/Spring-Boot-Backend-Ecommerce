package com.backend.service;

import com.backend.dto.OrderRequestDto;
import com.backend.model.OrderEntity;

public interface OrderService {
    // OrderResponseDto createOrder(OrderRequestDto orderRequestDto);
    OrderEntity createOrder(OrderRequestDto orderRequestDto);
}