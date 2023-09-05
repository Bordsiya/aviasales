package com.example.recommendationservice.model;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "city_experience")
public class CityExperience {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "city_experience_id_seq")
    @SequenceGenerator(name = "city_experience_id_seq", allocationSize = 1, sequenceName = "city_experience_id_seq")
    @Column(name = "id")
    @JsonView
    private Long id;

    @Column(name = "user_id")
    @JsonView
    private Long userId;

    @Column(name = "city")
    @JsonView
    private String city;

    @Column(name = "scrobbles")
    @JsonView
    private Long scrobbles;
}
