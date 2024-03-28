package com.backend.service;

import java.util.List;

import com.backend.dto.ProductDto;
import com.backend.dto.ProductsResponseDto;
import com.backend.model.ProductEntity;

public interface ProductService {

	List<ProductEntity> getAllProducts();

	ProductsResponseDto getAllProductsAdmin(int pageNo, int pageSize);

	ProductEntity getProductById(int id);

	void updateProductImage(int productId, String newImageUrl);

	ProductDto updateProduct(ProductDto oldProductDto);

	ProductDto createProduct();

}
