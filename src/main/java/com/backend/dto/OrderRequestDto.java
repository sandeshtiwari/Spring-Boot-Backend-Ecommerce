
package com.backend.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDto {

    // private int userId;
    private String username;
    private List<OrderItemRequestDto> items;
    private ShippingAddressDto shippingAddress;
    private String paymentMethod;

}
