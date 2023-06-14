package com.example.aviasales.client;

import java.util.List;

import com.example.aviasales.dto.RecommendationDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "recommendation-service",
        url = "${recommendation_service.url}"
)
public interface RecommendationClient {
    @GetMapping("/recommendation/{userId}")
    List<RecommendationDto> getAllUserRecommendations(@PathVariable long userId);
}
