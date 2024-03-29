package com.example.aviasales.entity;

import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Table(name = "aircrafts")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class Aircraft {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "aircrafts_id_seq")
    @SequenceGenerator(name = "aircrafts_id_seq", allocationSize = 1)
    @Column(name = "id")
    @JsonView
    private Long aircraftId;
    @Column(name = "manufacturer", nullable = false)
    @JsonView
    private String manufacturer;
    @Column(name = "model", nullable = false)
    @JsonView
    private String model;
    @Column(name = "number_of_seats", nullable = false)
    @JsonView
    private Integer numberOfSeats;
    @OneToMany(mappedBy = "aircraft", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnore
    private Set<Flight> flights;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JsonView
    @JoinColumn(name = "airline_id", referencedColumnName = "id")
    private Airline airline;


    @Override
    public String toString() {
        String ans = "Aircraft [" + "\n" +
                "aircraft-id=" + aircraftId + "\n" +
                ", manufacturer=" + manufacturer + "\n" +
                ", model=" + model + "\n" +
                ", number-of-seats=" + numberOfSeats + "\n" +
                ", flights=%s" + "\n" +
                "]";
        return String.format(ans, Objects.requireNonNullElse(flights, "[]"));
    }
}
