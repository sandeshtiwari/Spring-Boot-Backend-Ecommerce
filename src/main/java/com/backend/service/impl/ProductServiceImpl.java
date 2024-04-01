package com.backend.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.backend.dto.CategoryImageDto;
import com.backend.dto.ProductDto;
import com.backend.dto.ProductsResponseDto;
import com.backend.exceptions.ProductNotFoundException;
import com.backend.model.ProductEntity;
import com.backend.repository.ProductRepository;
import com.backend.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Override
	public List<ProductEntity> getAllProducts() {
		List<ProductEntity> allProducts = productRepository.findAll();
		return allProducts;
	}

	@Override
	public ProductEntity getProductById(int id) {
		ProductEntity product = productRepository.findById(id)
				.orElseThrow(() -> new ProductNotFoundException("Product with given Id not found"));
		return product;
	}

	// @Override
	// public List<ProductDto> getAllProductsAdmin(int pageNo, int pageSize) {
	// Pageable pageable = PageRequest.of(pageNo, pageSize);
	// Page<ProductEntity> allProducts = productRepository.findAll(pageable);
	// // List<ProductEntity> allProducts = productRepository.findAll();
	// List<ProductEntity> listOfProducts = allProducts.getContent();
	// List<ProductDto> allProductsDto = listOfProducts.stream().map(p ->
	// mapToDto(p)).collect(Collectors.toList());
	// return allProductsDto;
	// }

	@Override
	public ProductsResponseDto getAllProductsAdmin(int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		Page<ProductEntity> allProducts = productRepository.findAll(pageable);
		// List<ProductEntity> allProducts = productRepository.findAll();
		List<ProductEntity> listOfProducts = allProducts.getContent();
		List<ProductDto> allProductsDto = listOfProducts.stream().map(p -> mapToDto(p)).collect(Collectors.toList());

		ProductsResponseDto productsResponseDto = new ProductsResponseDto(allProductsDto, productRepository.count());
		return productsResponseDto;
	}

	private ProductDto mapToDto(ProductEntity product) {
		ProductDto productDto = new ProductDto();
		productDto.setProductId(product.getProductId());
		productDto.setName(product.getName());
		productDto.setImage(product.getImage());
		productDto.setBrand(product.getBrand());
		productDto.setCategory(product.getCategory());
		productDto.setDescription(product.getDescription());
		productDto.setNumReviews(product.getNumReviews());
		productDto.setPrice(product.getPrice());
		productDto.setCountInStock(product.getCountInStock());
		return productDto;
	}

	private ProductEntity mapToProductEntity(ProductDto productDto) {
		ProductEntity productEntity = new ProductEntity();
		productEntity.setProductId(productDto.getProductId());
		productEntity.setName(productDto.getName());
		productEntity.setImage(productDto.getImage());
		productEntity.setBrand(productDto.getBrand());
		productEntity.setCategory(productDto.getCategory());
		productEntity.setDescription(productDto.getDescription());
		productEntity.setNumReviews(productDto.getNumReviews());
		productEntity.setPrice(productDto.getPrice());
		productEntity.setCountInStock(productDto.getCountInStock());
		return productEntity;
	}

	@Override
	public void updateProductImage(int productId, String newImageUrl) {
		ProductEntity product = productRepository.findById(productId)
				.orElseThrow(() -> new ProductNotFoundException("Product with given Id not found"));
		product.setImage(newImageUrl);
		productRepository.save(product);
	}

	@Override
	public ProductDto updateProduct(ProductDto productDto) {
		ProductEntity productEntity = productRepository.findById(productDto.getProductId())
				.orElseThrow(() -> new ProductNotFoundException("Product not found to update"));
		productEntity.setBrand(productDto.getBrand());
		productEntity.setName(productDto.getName());
		productEntity.setCategory(productDto.getCategory());
		productEntity.setCountInStock(productDto.getCountInStock());
		productEntity.setDescription(productDto.getDescription());
		productEntity.setPrice(productDto.getPrice());
		ProductEntity savedProductEntity = productRepository.save(productEntity);
		return mapToDto(savedProductEntity);
	}

	@Override
	public ProductDto createProduct() {
		ProductEntity newProduct = new ProductEntity();
		newProduct.setName("");
		newProduct.setBrand("");
		newProduct.setCategory("");
		newProduct.setDescription("");
		productRepository.save(newProduct);
		return mapToDto(newProduct);
	}

	@Override
	public Map<String, String> deleteProduct(ProductDto productDto) throws ProductNotFoundException {
		ProductEntity productEntity = mapToProductEntity(productDto);
		productRepository.findById(productEntity.getProductId())
				.orElseThrow(() -> new ProductNotFoundException("Failed to delete product!"));
		productRepository.delete(productEntity);
		Map<String, String> messageMap = new HashMap<String, String>();
		messageMap.put("message", "Product deleted successfully!");
		return messageMap;
	}

	@Override
	public List<CategoryImageDto> getDistinctCategoriesWithImages() {
		return productRepository.findDistinctCategoriesWithImages();
	}

	@Override
	public List<ProductDto> getProductsByCategory(String category) {
		List<ProductEntity> productEntities = productRepository.findByCategory(category);
		return productEntities.stream().map(this::mapToDto).collect(Collectors.toList());
	}

}
