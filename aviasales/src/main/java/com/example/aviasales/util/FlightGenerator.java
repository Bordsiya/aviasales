package com.example.aviasales.util;

import com.example.aviasales.entity.Flight;
import org.springframework.stereotype.Component;

@Component
public class FlightGenerator {
    public Flight generateNewFlight(){
        return new Flight(

        );
    }


}
