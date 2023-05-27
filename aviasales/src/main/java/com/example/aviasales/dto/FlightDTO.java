package com.example.aviasales.dto;

import com.example.aviasales.entity.Aircraft;
import com.example.aviasales.entity.Airport;
import com.example.aviasales.entity.Passenger;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FlightDTO {
    @JsonView
    @NotNull(message = "Departure-airport-id cannot be null.")
    @Min(1)
    private Long departureAirportId;

    @JsonView
    @NotNull(message = "Arrival-airport-id cannot be null.")
    @Min(1)
    private Long arrivalAirportId;

    @JsonView
    @NotNull(message = "Arrival-date cannot be null.")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate departureDate;

    @JsonView
    @NotNull(message = "Arrival-date cannot be null.")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate arrivalDate;

    @JsonView
    @NotBlank(message = "Departure-time is required.")
    private String departureTime;

    @JsonView
    @NotBlank(message = "Arrival-time is required.")
    private String arrivalTime;

    @JsonView
    @NotNull(message = "Default-price-for-kids cannot be null.")
    @Min(0)
    private Long defaultPriceForKids;

    @JsonView
    @NotNull(message = "Default-price-for-adults cannot be null.")
    @Min(0)
    private Long defaultPriceForAdults;

    @JsonView
    @NotNull(message = "Aircraft-id cannot be null.")
    @Min(1)
    private Long aircraftId;
}
