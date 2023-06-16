package com.example.recommendationservice.service;

import java.util.List;

import com.example.recommendationservice.model.Recommendation;
import com.example.recommendationservice.repo.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecommendationService {
    private final RecommendationRepository repository;

    public List<Recommendation> getAllForUser(Long userId) {
        return repository.findAllByUserId(userId);
    }

    public Recommendation save(Recommendation recommendation) {
        return repository.save(recommendation);
    }
}
