package com.example.aviasales.repo;

import com.example.aviasales.entity.Airline;
import com.example.aviasales.entity.Tariff;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AirlineRepository extends JpaRepository<Airline, Long> {

}
