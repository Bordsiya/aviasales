package com.example.recommendationservice.service.event;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Random;

import com.example.recommendationservice.entity.Recommendation;
import com.example.recommendationservice.enums.RecommendationType;
import com.example.recommendationservice.repo.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BuyTicketEventWorker {
    private final BuyTicketEventRulesService service;

    private final RecommendationRepository repository;

    public Recommendation process(BuyTicketEvent event) {
        var found = service.getRecommendations(event);
        if (found.isEmpty()) {
            System.out.println("Found empty recommendations, nothing to save!");
            return null;
        }

        String randomRecommendation = found
                .stream()
                .skip(new Random().nextInt(found.size()))
                .findFirst()
                .get();

        var toSave = constructNew(event, randomRecommendation);
        return repository.save(toSave);
    }

    private Recommendation constructNew(BuyTicketEvent event, String recommendationCity) {
        var toSave = new Recommendation();
        toSave.setCreatedDate(LocalDate.now());
        toSave.setUserId(event.userId());
        toSave.setType(RecommendationType.CITY);
        toSave.setText(String.format("Привет! Попробуй посетить ещё и город %s", recommendationCity));
        return toSave;
    }
}
