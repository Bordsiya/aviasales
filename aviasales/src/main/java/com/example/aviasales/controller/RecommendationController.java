package com.example.aviasales.controller;

import java.security.Principal;
import java.util.List;

import com.example.aviasales.client.RecommendationClient;
import com.example.aviasales.dto.RecommendationDto;
import com.example.aviasales.entity.User;
import com.example.aviasales.exception.not_found.UserNotFoundException;
import com.example.aviasales.repo.UserRepository;
import com.example.aviasales.service.RecommendationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/public/recommendation")
@RequiredArgsConstructor
@Tag(name = "Контроллер рекомендаций", description = "Описание возможностей рекомендаций")
@SecurityRequirement(name = "basicAuth")
public class RecommendationController {
    private final RecommendationService recommendationService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RecommendationDto>> getAllUserRecommendations(Principal principal) {
        return ResponseEntity.ok(recommendationService.getAllUserRecommendations(principal.getName()));
    }
}
