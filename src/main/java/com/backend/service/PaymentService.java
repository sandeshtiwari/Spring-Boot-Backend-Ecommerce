package com.backend.service;

import java.util.Map;

import com.backend.dto.PaymentResponseDto;
import com.backend.model.OrderEntity;
import com.stripe.exception.StripeException;

public interface PaymentService {

    // public PaymentResponseDto createPaymentLink(OrderResponseDto
    // orderResponseDto) throws StripeException;
    public PaymentResponseDto createPaymentLink(OrderEntity orderEntity) throws StripeException;

    public Map<String, String> validateOrderSuccessToken(String token);

}
