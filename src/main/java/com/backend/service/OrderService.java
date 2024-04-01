package com.backend.service;

import java.util.List;
import java.util.Map;

import com.backend.dto.OrderAdminResponseDto;
import com.backend.dto.OrderRequestDto;
import com.backend.dto.OrderResponseDto;
import com.backend.model.OrderEntity;

public interface OrderService {
    // OrderResponseDto createOrder(OrderRequestDto orderRequestDto);
    OrderEntity createOrder(OrderRequestDto orderRequestDto);

    List<OrderResponseDto> getAllUserOrders(String username);

    OrderAdminResponseDto getAllOrdersAdmin(int pageNo, int pageSize);

    Map<String, String> toggleDeliveryStatus(int orderId);

}