package com.example.recommendationservice.service;

import com.example.recommendationservice.exception.CityExperienceNotFoundException;
import com.example.recommendationservice.model.BuyTicketEvent;
import com.example.recommendationservice.model.CityExperience;
import com.example.recommendationservice.repo.CityExperienceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CityExperienceService {
    private final CityExperienceRepository cityExperienceRepository;

    public CityExperience addCityExperience(BuyTicketEvent event) {
        CityExperience cityExperience = cityExperienceRepository.findCityExperienceByUserIdAndCity(
                event.userId(), event.cityTo());
        if (cityExperience == null) {
            cityExperience = new CityExperience(
                    null,
                    event.userId(),
                    event.cityTo(),
                    1L
            );
        }
        else {
            cityExperience.setScrobbles(cityExperience.getScrobbles() + 1);
        }
        return cityExperienceRepository.save(cityExperience);
    }
}
