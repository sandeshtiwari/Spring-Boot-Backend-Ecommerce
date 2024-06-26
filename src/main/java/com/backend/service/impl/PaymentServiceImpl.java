package com.backend.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.dto.PaymentResponseDto;
import com.backend.model.OrderEntity;
import com.backend.repository.OrderRepository;
import com.backend.service.PaymentService;
import com.backend.service.TokenStorageService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private TokenStorageService tokenStorageService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderServiceImpl orderServiceImpl;

    private final String stripeSecretKey = "sk_test_51P09nj00Kbi1gt3xJwIXtXYLCDK8QrxFpDjOeJogtkpi6z7yIFFZG0NJgxX90imzM6vVAnDPrzcC2jojf3520taD00c2xT69tN";

    // @Override
    // public PaymentResponseDto createPaymentLink(OrderResponseDto
    // orderResponseDto) throws StripeException {

    // Stripe.apiKey = stripeSecretKey;

    // SessionCreateParams params = SessionCreateParams.builder()
    // .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
    // .setMode(SessionCreateParams.Mode.PAYMENT)
    // .setSuccessUrl("http://localhost:5173/payment/success/" +
    // orderResponseDto.getOrderId())
    // .setCancelUrl("http://localhost:5173/payment/fail")
    // .addLineItem(SessionCreateParams.LineItem.builder().setQuantity(1L)
    // .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
    // .setCurrency("usd")
    // .setUnitAmount((long) orderResponseDto.getTotalPrice() * 100)
    // .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
    // .setName("OrderId :" + orderResponseDto.getOrderId() + " User "
    // + orderResponseDto.getUserId())
    // .build())
    // .build())
    // .build())
    // .build();

    // Session session = Session.create(params);

    // PaymentResponseDto paymentResponseDto = new PaymentResponseDto();
    // paymentResponseDto.setPaymentUrl(session.getUrl());

    // return paymentResponseDto;
    // }

    @Override
    public PaymentResponseDto createPaymentLink(OrderEntity orderEntity) throws StripeException {

        Stripe.apiKey = stripeSecretKey;

        String token = tokenStorageService.generateTokenForOrderId(orderEntity.getOrderId());

        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:5173/payment?token=" + token)
                .setCancelUrl("http://localhost:5173/payment?fail=true")
                .addLineItem(SessionCreateParams.LineItem.builder().setQuantity(1L)
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("usd")
                                .setUnitAmount((long) orderEntity.getTotalPrice() * 100)
                                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName("OrderId :" + orderEntity.getOrderId() + " User "
                                                + orderEntity.getUser().getId())
                                        .build())
                                .build())
                        .build())
                .build();

        Session session = Session.create(params);

        PaymentResponseDto paymentResponseDto = new PaymentResponseDto();
        paymentResponseDto.setPaymentUrl(session.getUrl());

        return paymentResponseDto;
    }

    @Override
    public Map<String, String> validateOrderSuccessToken(String token) {
        Integer orderId = tokenStorageService.getOrderIdForToken(token);
        Map<String, String> paymentMessage = new HashMap<>();
        if (orderId != null) {
            Optional<OrderEntity> orderEntityOptional = orderServiceImpl.findOrderEntityById(orderId);
            if (orderEntityOptional.isPresent()) {
                OrderEntity orderEntity = orderEntityOptional.get();
                orderEntity.setPaid(true);
                orderEntity.setPaidAt(new Date());
                orderRepository.save(orderEntity);
                paymentMessage.put("status", "Success");
                paymentMessage.put("message", "Order Payment success!");
                return paymentMessage;
            }
            paymentMessage.put("status", "Failed");
            paymentMessage.put("message", "Order Payment Failed!");
            System.out.println(token);
            return paymentMessage;
        } else {
            paymentMessage.put("status", "Failed");
            paymentMessage.put("message", "Invalid Order Token!");
            return paymentMessage;
        }
    }

}
