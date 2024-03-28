package com.backend.dto;

import com.backend.model.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
	private String token;	
	private String firstName;
	private String lastName;
	private String email;
	private String username;
	private Role role;
}
