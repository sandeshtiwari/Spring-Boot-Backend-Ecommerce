package com.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.dto.AuthenticationResponse;
import com.backend.model.UserEntity;
import com.backend.service.AuthenticationService;

@RestController
@RequestMapping("/api")
public class AuthenticationController {

	private final AuthenticationService authService;

	public AuthenticationController(AuthenticationService authService) {
		this.authService = authService;
	}
	
	@PostMapping("/register")
	public ResponseEntity<AuthenticationResponse> register(@RequestBody UserEntity request) {
		System.out.println(request.getFirstName() +  " Here");
		return ResponseEntity.ok(authService.register(request));
	}
	
	@PostMapping("/login")
	public ResponseEntity<AuthenticationResponse> login(@RequestBody UserEntity request) {
		return ResponseEntity.ok(authService.authenticate(request));
	}
	
}