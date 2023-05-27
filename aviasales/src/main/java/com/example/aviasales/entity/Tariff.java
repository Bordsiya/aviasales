package com.example.aviasales.entity;

import com.example.aviasales.util.enums.TariffType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;

import javax.persistence.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tariffs")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class Tariff {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tariffs_id_seq")
    @SequenceGenerator(name = "tariffs_id_seq", allocationSize = 1)
    @Column(name = "id")
    @JsonView
    private Long tariffId;
    @Column(name = "tariff_type", nullable = false)
    @Enumerated(EnumType.STRING)
    @JsonView
    private TariffType tariffType;
    @Column(name = "tariff_name", nullable = false)
    @JsonView
    private String tariffName;
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "airline_id", referencedColumnName = "id")
    @JsonView
    private Airline airline;
    @Column(name = "price_for_kids", nullable = false)
    @JsonView
    private Long priceForKids;
    @Column(name = "price_for_adults", nullable = false)
    @JsonView
    private Long priceForAdults;

    @Column(name = "has_exchange", nullable = false)
    @JsonView
    private Boolean hasExchange;
    @Column(name = "has_refund", nullable = false)
    @JsonView
    private Boolean hasRefund;
    @Column(name = "has_baggage", nullable = false)
    @JsonView
    private Boolean hasBaggage;

    @Column(name = "amount_of_baggage")
    @JsonView
    private Long amountOfBaggage;
    @Column(name = "weight_per_baggage_in_kg")
    @JsonView
    private Long weightPerBaggageInKg;
    @Column(name = "sum_of_baggage_sides_in_cm")
    @JsonView
    private Long sumOfBaggageSidesInCm;

    @Column(name = "amount_of_hand_baggage", nullable = false)
    @JsonView
    private Long amountOfHandBaggage;
    @Column(name = "weight_per_hand_baggage_in_kg", nullable = false)
    @JsonView
    private Long weightPerHandBaggageInKg;
    @Column(name = "height_per_hand_baggage_in_cm", nullable = false)
    @JsonView
    private Long heightPerHandBaggageInCm;
    @Column(name = "width_per_hand_baggage_in_cm", nullable = false)
    @JsonView
    private Long widthPerHandBaggageInCm;
    @Column(name = "depth_per_hand_baggage_in_cm", nullable = false)
    @JsonView
    private Long depthPerHandBaggageInCm;

    @Override
    public String toString() {
        return "Tariff [" + "\n" +
                "tariff-id=" + tariffId + "\n" +
                ", tariff-type=" + tariffType + "\n" +
                ", airline=" + airline.toString() + "\n" +
                ", price-for-kids=" + priceForKids + "\n" +
                ", price-for-adults=" + priceForAdults + "\n" +
                ", has-exchange=" + hasExchange + "\n" +
                ", has-refund=" + hasRefund + "\n" +
                ", has-baggage=" + hasBaggage + "\n" +
                ", amount-of-baggage=" + amountOfBaggage + "\n" +
                ", weight-per-baggage-in-kg=" + weightPerBaggageInKg + "\n" +
                ", sum-of-baggage-sides-in-cm=" + sumOfBaggageSidesInCm + "\n" +
                ", amount-of-hand-baggage=" + amountOfHandBaggage + "\n" +
                ", weight-per-hand-baggage-in-kg=" + weightPerHandBaggageInKg + "\n" +
                ", height-per-hand-baggage-in-cm=" + heightPerHandBaggageInCm + "\n" +
                ", width-per-hand-baggage-in-cm=" + widthPerHandBaggageInCm + "\n" +
                ", depth-per-hand-baggage-in-cm=" + depthPerHandBaggageInCm + "\n" +
                "]";
    }
}
