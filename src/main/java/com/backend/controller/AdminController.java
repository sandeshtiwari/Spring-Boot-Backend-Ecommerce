package com.backend.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.backend.dto.FileHandlingDto;
import com.backend.dto.OrderAdminResponseDto;
import com.backend.dto.OrderResponseDto;
import com.backend.dto.ProductDto;
import com.backend.dto.ProductsResponseDto;
import com.backend.dto.UsersAdminResponseDto;
import com.backend.service.impl.FileUploadImpl;
import com.backend.service.impl.OrderServiceImpl;
import com.backend.service.impl.ProductServiceImpl;
import com.backend.service.impl.UserServiceImpl;

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

	@Autowired
	private OrderServiceImpl orderServiceImpl;

	@Autowired
	private UserServiceImpl userServiceImpl;

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
	public ProductDto createProduct() {
		return productServiceImpl.createProduct();
	}

	@DeleteMapping("/product/delete")
	public Map<String, String> deleteProduct(@RequestBody ProductDto productDto) {
		return productServiceImpl.deleteProduct(productDto);
	}

	@GetMapping("/orders")
	public ResponseEntity<OrderAdminResponseDto> getOrders(
			@RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
			@RequestParam(value = "pageSize", defaultValue = "2", required = false) int pageSize) {
		OrderAdminResponseDto orderAdminResponseDto = orderServiceImpl.getAllOrdersAdmin(pageNo, pageSize);
		return new ResponseEntity<>(orderAdminResponseDto, HttpStatus.OK);
	}

	@PutMapping("/delivery")
	public Map<String, String> toggleDeliverly(@RequestParam(value = "orderId", required = true) int orderId) {
		return orderServiceImpl.toggleDeliveryStatus(orderId);
	}

	@GetMapping("/users")
	public ResponseEntity<UsersAdminResponseDto> getUsers(
			@RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
			@RequestParam(value = "pageSize", defaultValue = "2", required = false) int pageSize) {
		UsersAdminResponseDto usersAdminResponseDto = userServiceImpl.getAllUserssAdmin(pageNo, pageSize);
		return new ResponseEntity<>(usersAdminResponseDto, HttpStatus.OK);
	}

}
