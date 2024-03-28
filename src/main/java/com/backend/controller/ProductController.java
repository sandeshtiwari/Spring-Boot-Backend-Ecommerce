package com.backend.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.backend.dto.FileHandlingDto;
import com.backend.model.ProductEntity;
import com.backend.service.impl.FileUploadImpl;
import com.backend.service.impl.ProductServiceImpl;

@RestController
@RequestMapping("/api/products")
public class ProductController {

	@Autowired
	private ProductServiceImpl productServiceImpl;

	// @Autowired
	// private FileUploadImpl fileUploadImpl;

	@GetMapping({ "", "/" })
	public ResponseEntity<List<ProductEntity>> getAllProducts() {
		return new ResponseEntity<>(productServiceImpl.getAllProducts(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProductEntity> getProductById(@PathVariable(name = "id") int id) {
		return new ResponseEntity<>(productServiceImpl.getProductById(id), HttpStatus.OK);
	}

	// @PostMapping("/upload")
	// public ResponseEntity<FileHandlingDto> uplaodImage(@RequestParam("image")
	// MultipartFile multipartFile)
	// throws IOException {
	// System.out.println("Image data received " + multipartFile);
	// FileHandlingDto fileHandlingDto = fileUploadImpl.uploadFile(multipartFile);
	// return new ResponseEntity<>(fileHandlingDto, HttpStatus.CREATED);
	// }

}
