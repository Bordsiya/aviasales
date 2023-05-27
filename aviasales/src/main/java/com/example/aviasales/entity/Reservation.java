package com.example.aviasales.entity;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "reservations")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reservations_id_seq")
    @SequenceGenerator(name = "reservations_id_seq", allocationSize = 1, sequenceName = "reservations_id_seq")
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
