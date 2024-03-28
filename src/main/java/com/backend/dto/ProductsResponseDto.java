package com.backend.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductsResponseDto {
    private List<ProductDto> products;
    private long totalNumberOfProducts;
}
