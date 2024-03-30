package com.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemResponseDto {

    private int orderItemId;

    private int quantity;

    private String image;

    private long price;

    private int orderEntityId;

    private int productEntityId;
}
