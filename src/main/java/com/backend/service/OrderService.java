package com.backend.service;

import com.backend.dto.OrderRequestDto;
import com.backend.dto.OrderResponseDto;

public interface OrderService {
    OrderResponseDto createOrder(OrderRequestDto orderRequestDto);
}