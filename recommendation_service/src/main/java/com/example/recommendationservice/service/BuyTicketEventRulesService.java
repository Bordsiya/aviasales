package com.example.recommendationservice.service;

import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import com.example.recommendationservice.model.BuyTicketEvent;
import com.example.recommendationservice.model.RecommendationRules;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BuyTicketEventRulesService {
    private final ObjectMapper mapper;

    private final FileReader fileReader;

    private RecommendationRules rules;

    @PostConstruct
    private void initRules() throws JsonProcessingException {
        var raw = fileReader.readFileAsString("recommendations.json");
        rules = mapper.readValue(raw, RecommendationRules.class);
    }

    public List<String> getRecommendations(BuyTicketEvent event) {
        if (rules == null) {
            return Collections.emptyList();
        }

        var rForCityFrom = rules.rules().get(event.cityFrom());
        var rForCityTo = rules.rules().get(event.cityTo());

        List<String> result = new java.util.ArrayList<>(Collections.emptyList());
        if (rForCityFrom != null) {
            result.addAll(rForCityFrom);
        }
        if (rForCityTo != null) {
            result.addAll(rForCityTo);
        }

        return result.stream().distinct().toList();
    }
}
