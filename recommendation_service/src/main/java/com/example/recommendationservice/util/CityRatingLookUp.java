package com.example.recommendationservice.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CityRatingLookUp implements Comparable<CityRatingLookUp> {
    private String city;
    private Double ratingValue;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CityRatingLookUp that = (CityRatingLookUp) obj;
        return city.equals(that.city) &&
                ratingValue.equals(that.ratingValue);
    }

    @Override
    public int compareTo(@NotNull CityRatingLookUp o) {
        double delta = ratingValue - o.ratingValue;
        if (delta > 0 ) return 1;
        if (delta < 0) return -1;
        return 0;
    }
}
