package com.backend.controller;

import org.springframework.web.bind.annotation.RestController;

import com.backend.dto.OrderRequestDto;
import com.backend.dto.PaymentResponseDto;
import com.backend.model.OrderEntity;
import com.backend.service.TokenStorageService;
import com.backend.service.impl.OrderServiceImpl;
import com.backend.service.impl.PaymentServiceImpl;
import com.stripe.exception.StripeException;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderServiceImpl orderServiceImpl;

    @Autowired
    private PaymentServiceImpl paymentServiceImpl;

    @Autowired
    private TokenStorageService tokenStorageService;

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

    @GetMapping("/payment/success")
    public Map<String, String> handlePaymentSuccess(@RequestParam String token) {
        Integer orderId = tokenStorageService.getOrderIdForToken(token);
        Map<String, String> paymentMessage = new HashMap<>();
        if (orderId != null) {
            Optional<OrderEntity> orderEntityOptional = orderServiceImpl.findOrderEntityById(orderId);
            if (orderEntityOptional.isPresent()) {
                OrderEntity orderEntity = orderEntityOptional.get();
                orderEntity.setPaid(true);
                orderEntity.setPaidAt(new Date());
                paymentMessage.put("status", "Success");
                paymentMessage.put("message", "Order Payment success!");
                return paymentMessage;
            }
            paymentMessage.put("status", "Failed");
            paymentMessage.put("message", "Order Payment Failed!");
            return paymentMessage;
        } else {
            paymentMessage.put("status", "Failed");
            paymentMessage.put("message", "Invalid Order Token!");
            return paymentMessage;
        }
    }

}
