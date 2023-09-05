package com.example.recommendationservice.service;

import com.example.recommendationservice.model.CityExperience;
import com.example.recommendationservice.repo.CityExperienceRepository;
import com.example.recommendationservice.util.CityRatingLookUp;
import com.example.recommendationservice.util.UserRatingLookUp;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.ListUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserBasedCityRecommenderService {
    private static final Long SIMILARITY_NUM = 50L;
    private static final Long MINIMAL_RATER = 3L;

    private final CityExperienceRepository cityExperienceRepository;

    private double calculateUserSim(Long userId, Long otherUserId) {
        double similarityScore = 0.0;
        double nomUser = 0.0;
        double nomOther = 0.0;
        int minNumCommon = 0;
        List<CityExperience> userCityExperiences = cityExperienceRepository.findCityExperienceByUserId(userId);
        HashMap<String, Long> userCityScrobblesMap = new HashMap<>();
        userCityExperiences.forEach(i -> userCityScrobblesMap.put(i.getCity(), i.getScrobbles()));

        List<CityExperience> otherUserCityExperiences = cityExperienceRepository.findCityExperienceByUserId(otherUserId);
        HashMap<String, Long> otherUserCityScrobblesMap = new HashMap<>();
        otherUserCityExperiences.forEach(i -> otherUserCityScrobblesMap.put(i.getCity(), i.getScrobbles()));

        double userAvg = userCityExperiences.stream().mapToDouble(CityExperience::getScrobbles).average().orElse(0D);
        double otherUserAvg = otherUserCityExperiences.stream().mapToDouble(CityExperience::getScrobbles).average().orElse(0D);
        for (String mid1: userCityScrobblesMap.keySet()) {
            for (String mid2 : otherUserCityScrobblesMap.keySet()) {
                if (mid1.equals(mid2)) {
                    minNumCommon++;
                    double userScore = userCityScrobblesMap.get(mid1) - userAvg;
                    double otherScore = otherUserCityScrobblesMap.get(mid2) - otherUserAvg;
                    similarityScore += (userScore) * (otherScore);
                    nomUser += Math.pow(userScore, 2);
                    nomOther += Math.pow(otherScore, 2);
                }
            }
        }
        if (minNumCommon >= 2 && similarityScore != 0.0 && nomUser != 0.0 && nomOther != 0.0) {
            return similarityScore / (Math.sqrt(nomUser * nomOther));
        }
        return -100.0;
    }

    private ArrayList<UserRatingLookUp> getUserSimilarity(Long userId) {
        ArrayList<UserRatingLookUp> similarityScore = new ArrayList<UserRatingLookUp>();

        for (Long otherUserId: cityExperienceRepository.findDistinctUserIds()){
            if (!otherUserId.equals(userId)){
                double cosineScore = calculateUserSim(userId, otherUserId);
                if (cosineScore != -100.0) {
                    similarityScore.add(new UserRatingLookUp(otherUserId, cosineScore));
                }
            }
        }
        similarityScore.sort(Collections.reverseOrder());
        return similarityScore;
    }

    public ArrayList<CityRatingLookUp> getSimilarRatings(Long userId) {
        ArrayList<UserRatingLookUp> similarityScore = getUserSimilarity(userId);
        long numNeighbors = Math.min(similarityScore.size(), SIMILARITY_NUM);
        ArrayList<CityRatingLookUp> similarityRatingList = new ArrayList<>();

        List<CityExperience> userCityExperiences = cityExperienceRepository.findCityExperienceByUserId(userId);
        List<String> visitedCities = userCityExperiences.stream().map(CityExperience::getCity).toList();
        List<String> unvisitedCities = cityExperienceRepository.findDistinctCities().stream().filter(e -> !visitedCities.contains(e)).toList();

        double userAvg = userCityExperiences.stream().mapToDouble(CityExperience::getScrobbles).average().orElse(0D);
        for (String city: unvisitedCities){
            int counter = 0;
            double norm = 0.0;
            double total = 0.0;
            for (int i = 0; i < numNeighbors; i++){
                UserRatingLookUp userRating = similarityScore.get(i);
                Double cosineScore = userRating.getRatingValue();
                Long otherUserId = userRating.getUserId();

                List<CityExperience> otherUserCityExperiences = cityExperienceRepository.findCityExperienceByUserId(otherUserId);
                HashMap<String, Long> otherUserCityScrobblesMap = new HashMap<>();
                otherUserCityExperiences.forEach(_i -> otherUserCityScrobblesMap.put(_i.getCity(), _i.getScrobbles()));
                double otherAvg = otherUserCityExperiences.stream().mapToDouble(CityExperience::getScrobbles).average().orElse(0D);

                if (otherUserCityScrobblesMap.containsKey(city)) {
                    double rating = otherUserCityScrobblesMap.get(city);
                    counter++;
                    norm += Math.abs(cosineScore);
                    total += (rating - otherAvg) * cosineScore;
                }
            }
            if (counter >= MINIMAL_RATER) {
                double predRating = userAvg + (total / norm);
                similarityRatingList.add(new CityRatingLookUp(city, predRating));
            }
        }
        similarityRatingList.sort(Collections.reverseOrder());
        return similarityRatingList;
    }

}
