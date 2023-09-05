package com.example.recommendationservice.service;

import java.time.LocalDate;
import java.util.Random;

import com.example.recommendationservice.enums.RecommendationType;
import com.example.recommendationservice.exception.CannotConstructRecommendationsException;
import com.example.recommendationservice.model.BuyTicketEvent;
import com.example.recommendationservice.model.Recommendation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BuyTicketEventWorker {
    private final static Logger log = LoggerFactory.getLogger(BuyTicketEventWorker.class);
    private final BuyTicketEventRulesService service;

    private final RecommendationService recommendationService;

    public Recommendation process(BuyTicketEvent event) {
        try {
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
            log.info("Processed buyTicketEvent, saving recommendation: {}", toSave);
            return recommendationService.save(toSave);
        }
        catch (CannotConstructRecommendationsException e) {
            System.out.println(e.getMessage());
            return null;
        }
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
