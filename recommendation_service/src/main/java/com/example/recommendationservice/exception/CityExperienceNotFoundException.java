package com.example.recommendationservice.exception;

public class CityExperienceNotFoundException extends RuntimeException {
    public CityExperienceNotFoundException(Long cityExperienceId) {
        super("CityExperience with id [" + cityExperienceId.toString() + "] not found in the system");
    }
}
