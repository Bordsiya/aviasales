package com.example.recommendationservice.repo;

import com.example.recommendationservice.model.CityExperience;
import com.example.recommendationservice.model.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityExperienceRepository extends JpaRepository<CityExperience, Long> {
    List<CityExperience> findCityExperienceByUserId(Long userId);
    CityExperience findCityExperienceByUserIdAndCity(Long userId, String city);
    @Query(value = "SELECT DISTINCT c.user_id FROM city_experience c", nativeQuery = true)
    List<Long> findDistinctUserIds();
    @Query(value = "SELECT DISTINCT c.city FROM city_experience c", nativeQuery = true)
    List<String> findDistinctCities();
}
