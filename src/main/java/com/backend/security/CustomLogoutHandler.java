package com.backend.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import com.backend.model.Token;
import com.backend.repository.TokenRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomLogoutHandler implements LogoutHandler {
	
	@Autowired
	private TokenRepository tokenRepository;

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		String authHeader = request.getHeader("Authorization");
//		System.out.println("Loging out Header " +authHeader);
		if(authHeader == null || !authHeader.startsWith("Bearer ")) {
			return;
		}
		System.out.println("Loging out " +request);
		String token = authHeader.substring(7);
		
		// get stored token from database
		Token storedToken = tokenRepository.findByToken(token).orElse(null);
		// invalidate the token i.e. make logout true
		if(token != null) {
			storedToken.setLoggedOut(true);
			tokenRepository.save(storedToken);
		}
		//
		
		
	}


}
