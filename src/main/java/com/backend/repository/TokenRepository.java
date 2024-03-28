package com.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.backend.model.Token;

public interface TokenRepository extends JpaRepository<Token, Integer> {
	
	@Query("""
			SELECT t FROM Token t WHERE t.user.id = :userId AND t.loggedOut = false
			""")
	List<Token> findAllTokenByUser(Integer userId);
	
	Optional<Token> findByToken(String token);
}
