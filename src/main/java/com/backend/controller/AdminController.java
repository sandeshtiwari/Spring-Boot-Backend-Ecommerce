package com.backend.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.backend.dto.FileHandlingDto;
import com.backend.dto.ProductDto;
import com.backend.dto.ProductsResponseDto;
import com.backend.service.impl.FileUploadImpl;
import com.backend.service.impl.ProductServiceImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

	@Autowired
	private ProductServiceImpl productServiceImpl;

	@Autowired
	private FileUploadImpl fileUploadImpl;

	@GetMapping({ "", "/" })
	public ResponseEntity<String> admin() {
		System.out.println("Inside Admin test controller");
		return ResponseEntity.ok("Admin API...");
	}

	// @GetMapping("/products")
	// public ResponseEntity<List<ProductDto>> getProducts(
	// @RequestParam(value = "pageNo", defaultValue = "0", required = false) int
	// pageNo,
	// @RequestParam(value = "pageSize", defaultValue = "5", required = false) int
	// pageSize) {
	// List<ProductDto> productDtos = productServiceImpl.getAllProductsAdmin(pageNo,
	// pageSize);
	// return new ResponseEntity<>(productDtos, HttpStatus.OK);
	// }

	@GetMapping("/products")
	public ResponseEntity<ProductsResponseDto> getProducts(
			@RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
			@RequestParam(value = "pageSize", defaultValue = "5", required = false) int pageSize) {
		ProductsResponseDto productsResponseDto = productServiceImpl.getAllProductsAdmin(pageNo, pageSize);
		return new ResponseEntity<>(productsResponseDto, HttpStatus.OK);
	}

	@PostMapping("/upload")
	public ResponseEntity<FileHandlingDto> uplaodImage(@RequestParam("image") MultipartFile multipartFile,
			@RequestParam(value = "productId", required = true) int productId)
			throws IOException {
		// System.out.println("Image data received " + multipartFile);
		// System.out.println("Product id received " + productId);
		FileHandlingDto fileHandlingDto = fileUploadImpl.uploadFile(multipartFile);
		// Update Image URL for the product
		productServiceImpl.updateProductImage(productId, fileHandlingDto.getSecuredUrl());
		return new ResponseEntity<>(fileHandlingDto, HttpStatus.CREATED);
	}

	@PutMapping("/product/update")
	public ProductDto updateProduct(@RequestBody ProductDto productDto) {
		// System.out.println("IN THE UPDATE ROUTE **************** " +
		// productDto.getPrice());
		// System.out.println("Updating product with name " + productDto.getName());
		return productServiceImpl.updateProduct(productDto);
	}

	@PostMapping("/product/create")
	public ProductDto postMethodName() {
		return productServiceImpl.createProduct();
	}

}