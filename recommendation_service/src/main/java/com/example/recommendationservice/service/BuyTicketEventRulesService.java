package com.example.recommendationservice.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import com.example.recommendationservice.exception.CannotConstructRecommendationsException;
import com.example.recommendationservice.model.BuyTicketEvent;
import com.example.recommendationservice.model.CityExperience;
import com.example.recommendationservice.model.RecommendationRules;
import com.example.recommendationservice.repo.CityExperienceRepository;
import com.example.recommendationservice.util.CityRatingLookUp;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BuyTicketEventRulesService {

    private final UserBasedCityRecommenderService userBasedCityRecommenderService;
    private final CityExperienceService cityExperienceService;

    public List<String> getRecommendations(BuyTicketEvent event) {
        cityExperienceService.addCityExperience(event);
        ArrayList<CityRatingLookUp> userRec = userBasedCityRecommenderService.getSimilarRatings(event.userId());
        int userSize = Math.min(userRec.size(), 10);
        ArrayList<CityRatingLookUp> userList = new ArrayList<> (userRec.subList(0, userSize));
        if (userSize == 0) {
            throw new CannotConstructRecommendationsException();
        }
        return userList.stream().map(CityRatingLookUp::getCity).toList();
    }
}
