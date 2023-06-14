package com.example.recommendationservice.controller;


import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import com.example.recommendationservice.entity.Recommendation;
import com.example.recommendationservice.service.RecommendationService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/recommendation")
@RequiredArgsConstructor
@Tag(name = "Контроллер рекомендация", description = "Описание возможностей рекомендаций")
public class RecommendationController {
    private final RecommendationService service;

    @GetMapping(
            path = "/{userId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<Recommendation>> getAllUserRecommendations(
            @PathVariable @Min(1) @Max(Long.MAX_VALUE)
            @Parameter(description = "id пользователя") Long userId
    ) {
        var found = service.getAllForUser(userId);
        return ResponseEntity.ok(found);
    }
}
