package com.example.aviasales.service;

import com.example.aviasales.client.RecommendationClient;
import com.example.aviasales.dto.RecommendationDto;
import com.example.aviasales.entity.User;
import com.example.aviasales.exception.not_found.UserNotFoundException;
import com.example.aviasales.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class RecommendationService {
    private final RecommendationClient client;
    private final UserRepository userRepository;

    @Autowired
    public RecommendationService(RecommendationClient recommendationClient,
                                 UserRepository userRepository) {
        this.client = recommendationClient;
        this.userRepository = userRepository;
    }

    public List<RecommendationDto> getAllUserRecommendations(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException(email);
        }

        return client.getAllUserRecommendations(user.getUserId());
    }
}
