package com.example.aviasales.service.camunda;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TokenService {
    private final static Map<String, String> userTokens = new HashMap<>();

    public static String getUserToken(String userId) {
        return userTokens.getOrDefault(userId, null);
    }

    public static void putUserToken(String userId, String token) {
        userTokens.put(userId, token);
    }
}
