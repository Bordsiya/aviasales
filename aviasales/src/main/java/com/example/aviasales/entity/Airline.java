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
@Table(name = "airlines")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class Airline {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "airlines_id_seq")
    @SequenceGenerator(name = "airlines_id_seq", allocationSize = 1)
    @Column(name = "id")
    @JsonView
    private Long airlineId;
    @Column(name = "name", nullable = false)
    @JsonView
    private String airlineName;
    @OneToMany(mappedBy = "airline", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnore
    private Set<Aircraft> aircrafts;
    @OneToMany(mappedBy = "airline", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnore
    private Set<Tariff> tariffs;


    @Override
    public String toString() {
        String ans = "Airline [" + "\n" +
                "airline-id=" + airlineId+ "\n" +
                ", airline-name=" + airlineName + "\n" +
                ", aircrafts=%s" + "\n" +
                ", tariffs=%s" + "\n" +
                "]";
        return String.format(ans,
                Objects.requireNonNullElse(aircrafts, "[]"),
                Objects.requireNonNullElse(tariffs, "[]"));
    }
}
