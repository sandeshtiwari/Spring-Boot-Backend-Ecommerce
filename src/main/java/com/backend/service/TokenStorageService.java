package com.backend.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class TokenStorageService {
    private final Map<String, Integer> tokenToOrderIdMap = new HashMap<>();

    public String generateTokenForOrderId(int orderId) {
        String token = UUID.randomUUID().toString();
        tokenToOrderIdMap.put(token, orderId);
        return token;
    }

    public Integer getOrderIdForToken(String token) {
        return tokenToOrderIdMap.get(token);
    }
}