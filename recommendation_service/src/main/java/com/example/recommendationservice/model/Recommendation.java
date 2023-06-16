package com.example.recommendationservice.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.example.recommendationservice.dto.RecommendationDto;
import com.example.recommendationservice.enums.RecommendationType;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "recommendation")
public class Recommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "recommendation_id_seq")
    @SequenceGenerator(name = "recommendation_id_seq", allocationSize = 1, sequenceName = "recommendation_id_seq")
    @Column(name = "id")
    @JsonView
    private Long id;

    @Column(name = "user_id")
    @JsonView
    private Long userId;

    @Column(name = "text")
    @JsonView
    private String text;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    @JsonView
    private RecommendationType type;

    @Column(name = "created_date")
    @JsonView
    private LocalDate createdDate;

    public RecommendationDto toDto() {
        return new RecommendationDto(
                id,
                userId,
                text,
                type,
                createdDate
        );
    }
}
