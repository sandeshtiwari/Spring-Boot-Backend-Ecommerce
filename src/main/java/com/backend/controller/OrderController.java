package com.backend.controller;

import org.springframework.web.bind.annotation.RestController;

import com.backend.dto.OrderRequestDto;
import com.backend.dto.PaymentResponseDto;
import com.backend.model.OrderEntity;
import com.backend.service.impl.OrderServiceImpl;
import com.backend.service.impl.PaymentServiceImpl;
import com.stripe.exception.StripeException;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderServiceImpl orderServiceImpl;

    @Autowired
    private PaymentServiceImpl paymentServiceImpl;

    // @PostMapping("/create")
    // public OrderResponseDto createNewOrder(@RequestBody OrderRequestDto
    // orderRequestDto) {
    // OrderResponseDto newOrderResponseDto =
    // orderServiceImpl.createOrder(orderRequestDto);
    // return newOrderResponseDto;
    // }

    @PostMapping("/create")
    public PaymentResponseDto createNewOrder(@RequestBody OrderRequestDto orderRequestDto) throws StripeException {
        // OrderResponseDto newOrderResponseDto =
        // orderServiceImpl.createOrder(orderRequestDto);
        OrderEntity newOrderEntity = orderServiceImpl.createOrder(orderRequestDto);

        PaymentResponseDto paymentResponseDto = paymentServiceImpl.createPaymentLink(newOrderEntity);
        return paymentResponseDto;
    }

}
