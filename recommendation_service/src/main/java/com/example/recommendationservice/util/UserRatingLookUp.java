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
public class UserRatingLookUp implements Comparable<UserRatingLookUp> {
    private Long userId;
    private Double ratingValue;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        UserRatingLookUp that = (UserRatingLookUp) obj;
        return userId.equals(that.userId) &&
                ratingValue.equals(that.ratingValue);
    }

    @Override
    public int compareTo(@NotNull UserRatingLookUp o) {
        double delta = ratingValue - o.ratingValue;
        if (delta > 0 ) return 1;
        if (delta < 0) return -1;
        return 0;
    }
}
