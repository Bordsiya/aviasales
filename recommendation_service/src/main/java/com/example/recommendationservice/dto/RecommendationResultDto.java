package com.example.recommendationservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RecommendationResultDto(
        @JsonProperty(value = "added")
        RecommendationDto recommendationDto,

        @JsonProperty(value = "status")
        String status
) {
}
