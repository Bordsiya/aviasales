package com.example.recommendationservice.service.event;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BuyTicketEventRulesService {
    private final ObjectMapper mapper;
    private RecommendationRules rules = null;

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

    @PostConstruct
    private void init() throws JsonProcessingException {
        InputStream is = getResourceFileAsInputStream("recommendations.json");
        if (is != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            var raw = (String) reader.lines().collect(Collectors.joining(System.lineSeparator()));
            rules = mapper.readValue(raw, RecommendationRules.class);
        } else {
            throw new RuntimeException("resource not found");
        }
    }

    private InputStream getResourceFileAsInputStream(String fileName) {
        ClassLoader classLoader = BuyTicketEventRulesService.class.getClassLoader();
        return classLoader.getResourceAsStream(fileName);
    }
}
