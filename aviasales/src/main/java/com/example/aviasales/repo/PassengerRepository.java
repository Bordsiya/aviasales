package com.example.aviasales.repo;

import com.example.aviasales.entity.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {
    @Query(value = "select count(*) from passengers where flight_id = :flightId", nativeQuery = true)
    public Long countByFlightId(@Param("flightId") Long flightId);
}
