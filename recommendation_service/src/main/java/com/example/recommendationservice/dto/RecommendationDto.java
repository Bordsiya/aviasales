package com.example.recommendationservice.dto;

import java.time.LocalDate;

import com.example.recommendationservice.enums.RecommendationType;
import com.fasterxml.jackson.annotation.JsonProperty;

public record RecommendationDto(
        @JsonProperty("id")
        Long id,

        @JsonProperty("user_id")
        Long userId,

        @JsonProperty("text")
        String text,

        @JsonProperty("type")
        RecommendationType type,

        @JsonProperty("created_date")
        LocalDate createdDate
) {
}
