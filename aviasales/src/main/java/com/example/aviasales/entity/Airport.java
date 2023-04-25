package com.example.aviasales.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "airports")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class Airport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JsonView
    private Long airportId;
    @Column(name = "code", nullable = false)
    @JsonView
    private String airportCode;
    @Column(name = "name", nullable = false)
    @JsonView
    private String airportName;
    @Column(name = "city", nullable = false)
    @JsonView
    private String city;
    @Column(name = "state")
    @JsonView
    private String state;
    @Column(name = "country", nullable = false)
    @JsonView
    private String country;
    @OneToMany(mappedBy = "departureAirport")
    @JsonIgnore
    Set<Flight> flightsFrom;
    @OneToMany(mappedBy = "arrivalAirport")
    @JsonIgnore
    Set<Flight> flightsTo;

    @Override
    public String toString() {
        String ans = "Airport [" + "\n" +
                "airport-id=" + airportId + "\n" +
                ", airport-code=" + airportCode + "\n" +
                ", airport-name=" + airportName + "\n" +
                ", city=" + city + "\n" +
                ", state=" + state + "\n" +
                ", country=" + country + "\n" +
                ", flights-from=%s" + "\n" +
                ", flights-to=%s" + "\n" +
                "]";
        return String.format(ans,
                Objects.requireNonNullElse(flightsFrom, "[]"),
                Objects.requireNonNullElse(flightsTo, "[]"));
    }
}
