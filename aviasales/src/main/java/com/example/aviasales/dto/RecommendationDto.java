package com.example.aviasales.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RecommendationDto(
        @JsonProperty("id")
        Long id,

        @JsonProperty("user_id")
        Long userId,

        @JsonProperty("text")
        String text,

        @JsonProperty("type")
        String type,

        @JsonProperty("created_date")
        LocalDate createdDate
) {
}
