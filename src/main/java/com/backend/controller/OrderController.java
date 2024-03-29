package com.backend.controller;

import org.springframework.web.bind.annotation.RestController;

import com.backend.dto.OrderRequestDto;
import com.backend.dto.OrderResponseDto;
import com.backend.service.impl.OrderServiceImpl;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderServiceImpl orderServiceImpl;

    @PostMapping("/create")
    public OrderResponseDto createNewOrder(@RequestBody OrderRequestDto orderRequestDto) {
        OrderResponseDto newOrderResponseDto = orderServiceImpl.createOrder(orderRequestDto);
        return newOrderResponseDto;
    }

}
