package com.example.aviasales.repo;

import com.example.aviasales.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {
    @Query(value = "select * from flights fl " +
            "where (fl.arrival_airport_id = :airportToId \n" +
            "and fl.departure_airport_id = :airportFromId \n" +
            "and fl.departure_time >= :departureTimeFrom \n" +
            "and fl.arrival_time >= :arrivalTimeFrom \n" +
            "and extract(epoch from fl.arrival_time - fl.departure_time) / 3600 <= :flightDurationTimeUntilInHH \n" +
            "and fl.departure_date = :dateFrom)"
            , nativeQuery = true)
    Set<Flight> getFlightsSearched(
        @Param("airportToId") Long airportToId,
        @Param("airportFromId") Long airportFromId,
        @Param("departureTimeFrom") LocalTime departureTimeFrom,
        @Param("arrivalTimeFrom") LocalTime arrivalTimeFrom,
        @Param("flightDurationTimeUntilInHH") Long flightDurationTimeUntilInHH,
        @Param("dateFrom") LocalDate dateFrom
    );

    @Query(value = "select id from flights " +
            "where aircraft_id = :aircraftId " +
            "and (departure_date >= :departureDate and arrival_date <= :arrivalDate)"
    , nativeQuery = true)
    Long getFlightIdWithAircraftIdBetweenDepartureDateAndArrivalDate(
            @Param("aircraftId") Long aircraftId,
            @Param("departureDate") LocalDate departureDate,
            @Param("arrivalDate") LocalDate arrivalDate
    );
}
