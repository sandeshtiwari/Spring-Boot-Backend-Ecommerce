package com.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
	private int productId;
	
	private String name;
	
	private String image;
	
	private String brand;
	
	private String category;
	
	private String description;
	
	private int rating;
	
	private long numReviews = 0;
	
	private long price;

	private long countInStock;
}
