package com.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product")
public class ProductEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_id")
	private int productId;

	@Column(name = "name")
	private String name;

	@Column(name = "image")
	private String image;

	@Column(name = "brand")
	private String brand;

	@Column(name = "category")
	private String category;

	@Column(name = "description")
	private String description;

	@Column(name = "rating")
	private int rating;

	@Column(name = "num_reviews")
	private long numReviews = 0;

	@Column(name = "price")
	private long price;

	@Column(name = "count_in_stock")
	private long countInStock;

}
