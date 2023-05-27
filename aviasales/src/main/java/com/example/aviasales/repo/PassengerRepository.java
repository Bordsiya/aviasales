package com.example.aviasales.repo;

import com.example.aviasales.entity.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {
    @Query(value = "select count(*) from passengers where flight_id = :flightId", nativeQuery = true)
    Long countByFlightId(@Param("flightId") Long flightId);

    @Query(value = "select id from passengers \n" +
            "inner join reservations on passengers.reservation_id = reservations.id \n" +
            "where reservations.id = :reservationId", nativeQuery = true)
    Set<Long> getPassengersBySameReservation(@Param("reservationId") Long reservationId);
}
