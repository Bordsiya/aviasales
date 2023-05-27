package com.example.aviasales.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "reservations")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JsonView
    private Long reservationId;
    @Column(name = "code", nullable = false)
    @JsonView
    private String reservationCode;
    @Column(name = "time", nullable = false)
    @JsonView
    private LocalDateTime time;
    @Column(name = "phone_number", nullable = false)
    @JsonView
    private String phoneNumber;
    @Column(name = "email", nullable = false)
    @JsonView
    private String email;
    @OneToMany(mappedBy = "reservation", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnore
    private Set<Passenger> passengers;

    @Override
    public String toString() {
        String ans = "Reservation [" + "\n" +
                "reservation-id=" + reservationId + "\n" +
                ", reservation-code=" + reservationCode + "\n" +
                ", time=" + time.toString() + "\n" +
                ", phone-number=" + phoneNumber + "\n" +
                ", email=" + email + "\n" +
                ", passengers=%s" + "\n" +
                "]";
        return String.format(ans,
                Objects.requireNonNullElse(passengers, "[]"));
    }
}
