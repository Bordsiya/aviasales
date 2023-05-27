package com.example.aviasales.entity;

import com.example.aviasales.util.enums.DocumentType;
import com.example.aviasales.util.enums.Gender;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "passengers")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class Passenger {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "passengers_id_seq")
    @SequenceGenerator(name = "passengers_id_seq", allocationSize = 1, sequenceName = "passengers_id_seq")
    @Column(name = "id")
    @JsonView
    private Long passengerId;
    @Column(name = "first_name", nullable = false)
    @JsonView
    private String firstName;
    @Column(name = "last_name", nullable = false)
    @JsonView
    private String lastName;
    @Column(name = "patronymic", nullable = false)
    @JsonView
    private String patronymic;
    @Column(name = "gender", nullable = false)
    @Enumerated(EnumType.STRING)
    @JsonView
    private Gender gender;
    @Column(name = "citizenship", nullable = false)
    @JsonView
    private String citizenship;
    @Column(name = "is_kid", nullable = false)
    @JsonView
    private Boolean isKid;
    @Column(name = "document_type", nullable = false)
    @Enumerated(EnumType.STRING)
    @JsonView
    private DocumentType documentType;
    @Column(name = "document_number", nullable = false)
    @JsonView
    private String documentNumber;
    @Column(name = "expiration_date")
    @JsonView
    private LocalDate expirationDate;
    @Column(name = "has_hearing_difficulties", nullable = false)
    @JsonView
    private Boolean hasHearingDifficulties;
    @Column(name = "has_vision_difficulties", nullable = false)
    @JsonView
    private Boolean hasVisionDifficulties;
    @Column(name = "required_wheelchair", nullable = false)
    @JsonView
    private Boolean requiredWheelchair;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_id", referencedColumnName = "id")
    @JsonView
    private Flight flight;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "tariff_id", referencedColumnName = "id")
    @JsonView
    private Tariff tariff;
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "reservation_id", referencedColumnName = "id")
    @JsonView
    private Reservation reservation;

    @Override
    public String toString() {
        return "Passenger [" + "\n" +
                "id=" + passengerId + "\n" +
                ", first-name=" + firstName + "\n" +
                ", last-name=" + lastName + "\n" +
                ", patronymic=" + patronymic + "\n" +
                ", gender=" + gender + "\n" +
                ", citizenship=" + citizenship + "\n" +
                ", document-type=" + documentType + "\n" +
                ", document-number=" + documentNumber + "\n" +
                ", expiration-date=" + expirationDate + "\n" +
                ", has-hearing-difficulties=" + hasHearingDifficulties + "\n" +
                ", has-vision-difficulties=" + hasVisionDifficulties + "\n" +
                ", required-wheelchair=" + requiredWheelchair + "\n" +
                ", flight=" + flight.toString() + "\n" +
                ", reservation=" + reservation.toString() + "\n" +
                "]";
    }
}
