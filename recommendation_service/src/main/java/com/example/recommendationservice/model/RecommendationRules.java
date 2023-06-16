package com.example.recommendationservice.model;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RecommendationRules(
        @JsonProperty("rules")
        Map<String, List<String>> rules
) {

}
