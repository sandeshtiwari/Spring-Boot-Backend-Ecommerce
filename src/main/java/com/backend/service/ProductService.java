package com.backend.service;

import java.util.List;
import java.util.Map;

import com.backend.dto.CategoryImageDto;
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

	Map<String, String> deleteProduct(ProductDto productDto);

	List<CategoryImageDto> getDistinctCategoriesWithImages();

	List<ProductDto> getProductsByCategory(String category);

}
