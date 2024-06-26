package com.backend.dto;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDto {
    private int orderId;
    private Date createdAt;
    private Date deliveredAt;
    private boolean isDelivered;
    private boolean isPaid;
    private String paymentMethod;
    private long itemsPrice;
    private Date paidAt;
    private long taxPrice;
    private long shippingPrice;
    private long totalPrice;
    private int userId;
    private ShippingAddressDto shippingAddress;
    private List<OrderItemResponseDto> orderItems;
}
