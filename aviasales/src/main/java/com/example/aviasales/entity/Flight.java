package com.example.aviasales.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "flights")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "flights_id_seq")
    @SequenceGenerator(name = "flights_id_seq", allocationSize = 1)
    @Column(name = "id")
    @JsonView
    private Long flightId;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "departure_airport_id", referencedColumnName = "id")
    @JsonView
    private Airport departureAirport;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "arrival_airport_id", referencedColumnName = "id")
    @JsonView
    private Airport arrivalAirport;
    @Column(name = "departure_date", nullable = false)
    @JsonView
    private LocalDate departureDate;
    @Column(name = "arrival_date", nullable = false)
    @JsonView
    private LocalDate arrivalDate;
    @Column(name = "departure_time", nullable = false)
    @JsonView
    private LocalTime departureTime;
    @Column(name = "arrival_time", nullable = false)
    @JsonView
    private LocalTime arrivalTime;
    @Column(name = "default_price_for_kids", nullable = false)
    @JsonView
    private Long defaultPriceForKids;
    @Column(name = "default_price_for_adults", nullable = false)
    @JsonView
    private Long defaultPriceForAdults;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "aircraft_id", referencedColumnName = "id")
    @JsonView
    private Aircraft aircraft;
    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "flight")
    @JsonIgnore
    List<Passenger> passengers;

    @Override
    public String toString() {
        String ans = "Flight [" + "\n" +
                "flight-id=" + flightId + "\n" +
                ", departure-airport=" + departureAirport + "\n" +
                ", arrival-airport=" + arrivalAirport + "\n" +
                ", departure-date=" + departureDate + "\n" +
                ", arrival-date=" + arrivalDate + "\n" +
                ", departure-time=" + departureTime + "\n" +
                ", arrival-time=" + arrivalTime + "\n" +
                ", default-price-for-kids=" + defaultPriceForKids + "\n" +
                ", default-price-for-adults=" + defaultPriceForAdults + "\n" +
                ", aircraft=" + aircraft.toString() + "\n" +
                ", passengers=%s" + "\n" +
                "]";
        return String.format(ans, Objects.requireNonNullElse(passengers, "[]"));
    }
}
