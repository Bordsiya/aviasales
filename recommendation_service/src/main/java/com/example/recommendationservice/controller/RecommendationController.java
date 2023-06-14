package com.example.recommendationservice.controller;


import java.util.Comparator;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import com.example.recommendationservice.dto.RecommendationDto;
import com.example.recommendationservice.dto.RecommendationResultDto;
import com.example.recommendationservice.entity.Recommendation;
import com.example.recommendationservice.service.RecommendationService;
import com.example.recommendationservice.service.event.BuyTicketEvent;
import com.example.recommendationservice.service.event.BuyTicketEventWorker;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/recommendation")
@RequiredArgsConstructor
@Tag(name = "Контроллер рекомендация", description = "Описание возможностей рекомендаций")
public class RecommendationController {
    private final RecommendationService service;

    private final BuyTicketEventWorker worker;

    @GetMapping(
            path = "/{userId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<RecommendationDto>> getAllUserRecommendations(
            @PathVariable @Min(1) @Max(Long.MAX_VALUE)
            @Parameter(description = "id пользователя") Long userId
    ) {
        var found = service.getAllForUser(userId)
                .stream()
                .sorted(
                        Comparator
                                .comparing(Recommendation::getCreatedDate)
                                .reversed()
                                .thenComparing(Recommendation::getId)
                )
                .map(Recommendation::toDto)
                .toList();
        return ResponseEntity.ok(found);
    }

    @PostMapping(
            path = "/force",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<RecommendationResultDto> forceBuyTicketEvent(
            @Valid @RequestBody BuyTicketEvent event
    ) {
        var processed = worker.process(event);
        var toReturn = new RecommendationResultDto(
                processed != null ? processed.toDto() : null,
                processed != null ? "SUCCESS" : "NOT_FOUND"
        );
        return ResponseEntity.ok(toReturn);
    }
}
