package com.example.aviasales.repo;

import com.example.aviasales.entity.Airline;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AirlineRepository extends JpaRepository<Airline, Long> {

}
