package com.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.backend.dto.AuthenticationResponse;
import com.backend.exceptions.UserAlreadyExistsException;
import com.backend.model.Role;
import com.backend.model.Token;
import com.backend.model.UserEntity;
import com.backend.repository.TokenRepository;
import com.backend.repository.UserRepository;

@Service
public class AuthenticationService {

	@Autowired
	private UserRepository repository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private JWTService jwtService;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private TokenRepository tokenRepository;

	// public AuthenticationService(UserRepository repository, PasswordEncoder
	// passwordEncoder, JWTService jwtService,
	// AuthenticationManager authenticationManager, TokenRepository tokenRepository)
	// {
	// this.repository = repository;
	// this.passwordEncoder = passwordEncoder;
	// this.jwtService = jwtService;
	// this.authenticationManager = authenticationManager;
	// this.tokenRepository = tokenRepository;
	// }

	public AuthenticationResponse register(UserEntity request) {
		System.out.println(request.getFirstName());

		// Check if there is a user with the same user name
		Optional<UserEntity> checkUser = repository.findByUsername(request.getUsername());
		if (checkUser.isPresent()) {
			throw new UserAlreadyExistsException("User with username " + request.getUsername() + " already exists");
		}

		UserEntity user = new UserEntity();
		user.setFirstName(request.getFirstName());
		user.setLastName(request.getLastName());
		user.setEmail(request.getEmail());
		user.setUsername(request.getUsername());
		user.setPassword(passwordEncoder.encode(request.getPassword()));

		// For testing purpose only lparajuli is the ADMIN
		if (request.getUsername().equals("lparajuli")) {
			user.setRole(Role.ADMIN);
		} else {
			user.setRole(Role.USER);
		}

		user = repository.save(user);

		String jwt = jwtService.generateToken(user);

		// save the generated token
		saveUserToken(user, jwt);

		AuthenticationResponse authResponseDto = generateAuthenticationResponse(user, jwt);

		return authResponseDto;

	}

	private AuthenticationResponse generateAuthenticationResponse(UserEntity user, String jwt) {
		AuthenticationResponse authResponseDto = new AuthenticationResponse();
		authResponseDto.setFirstName(user.getFirstName());
		authResponseDto.setLastName(user.getLastName());
		authResponseDto.setEmail(user.getEmail());
		authResponseDto.setUsername(user.getUsername());
		authResponseDto.setRole(user.getRole());
		authResponseDto.setToken(jwt);
		return authResponseDto;
	}

	// public boolean isTokenValid(UserEntity request){
	// authenticationManager.authenticate(
	// new UsernamePasswordAuthenticationToken(request.getUsername(),
	// request.getPassword()));
	// UserEntity user =
	// repository.findByUsername(request.getUsername()).orElseThrow();
	// }

	public AuthenticationResponse authenticate(UserEntity request) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
		UserEntity user = repository.findByUsername(request.getUsername()).orElseThrow();
		String token = jwtService.generateToken(user);

		revokeAllTokenByUser(user);
		// save the generated token
		saveUserToken(user, token);

		AuthenticationResponse authResponseDto = generateAuthenticationResponse(user, token);

		return authResponseDto;
	}

	private void revokeAllTokenByUser(UserEntity user) {
		List<Token> validTokenListByUser = tokenRepository.findAllTokenByUser(user.getId());
		if (!validTokenListByUser.isEmpty()) {
			validTokenListByUser.forEach(t -> {
				t.setLoggedOut(true);
			});
		}
	}

	private void saveUserToken(UserEntity user, String jwt) {
		Token token = new Token();
		token.setToken(jwt);
		token.setLoggedOut(false);
		token.setUser(user);
		tokenRepository.save(token);
	}

}
