package com.example.aviasales.util.mappers;

import com.example.aviasales.util.FlightQuery;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FlightQueryMapper {

    public FlightQuery objectToFlightQueryMapper(Object[] object) {
        Long flightId = Long.parseLong(String.valueOf(object[0]));
        Long minPrice = Long.parseLong(String.valueOf(object[1]));
        long[] tariffs = (long[]) object[2];
        List<Long> tariffIds = new ArrayList<>();
        for (long tariffId: tariffs) {
            tariffIds.add(tariffId);
        }
        return new FlightQuery(
                flightId,
                minPrice,
                tariffIds
        );
    }

    public List<FlightQuery> listObjectsToListFlightQueryMapper(List<Object> result) {
        List<FlightQuery> flightQueries = new ArrayList<>();
        for (Object o : result) {
            Object[] obj = (Object[]) o;
            flightQueries.add(objectToFlightQueryMapper(obj));
        }
        return flightQueries;
    }
}
