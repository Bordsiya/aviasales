package com.example.recommendationservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BuyTicketEvent(
        @JsonProperty(value = "userId", required = true)
        Long userId,
        @JsonProperty(value = "cityFrom", required = true)
        String cityFrom,
        @JsonProperty(value = "cityTo", required = true)
        String cityTo
) {

}
