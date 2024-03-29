package com.backend.dto;

import java.util.List;

import com.backend.model.OrderItemsEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDto {
    private int orderId;
    private String paymentMethod;
    private long itemsPrice;
    private long taxPrice;
    private long shippingPrice;
    private long totalPrice;
    private int userId;
    private ShippingAddressDto shippingAddressDto;
    private List<OrderItemsEntity> orderItemsEntity;
}
