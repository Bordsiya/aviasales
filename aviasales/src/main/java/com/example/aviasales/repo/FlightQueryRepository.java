package com.example.aviasales.repo;

import com.example.aviasales.util.FlightQuery;
import com.example.aviasales.util.enums.SortingAlgorithm;
import com.example.aviasales.util.mappers.FlightQueryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component
public class FlightQueryRepository {
    private static final Logger log = LoggerFactory.getLogger(FlightQueryRepository.class);
    @PersistenceContext
    EntityManager entityManager;
    @Autowired
    FlightQueryMapper flightQueryMapper;

    public List<FlightQuery> findFlightQueriesByFilters(
            Long airportFromId,
            Long airportToId,
            LocalDate dateFrom,
            Long amountOfAdults,
            Long amountOfChildren,
            String tariffType,
            Boolean hasBaggage,
            LocalTime departureTimeFrom,
            LocalTime arrivalTimeFrom,
            Long flightDurationTimeUntilInHH,
            Long maxPrice,
            SortingAlgorithm sortingAlgorithm,
            Long limit,
            Long offset
    ){
        String orderBy = "max(res." + sortingAlgorithm.sortingField + ")";
        log.info(orderBy);
        return flightQueryMapper.listObjectsToListFlightQueryMapper(
                (List<Object>) entityManager.createNativeQuery(
                        "select res.fl_id, max(res.min_price) as mn_prc, array_agg(res.tf_id), max(res.departure_time) as dptr, max(res.arrival_time) as artm from" +
                        "(select min_price, fl.id as fl_id, tf.id as tf_id, fl.departure_time as departure_time, fl.arrival_time as arrival_time from flights fl\n" +
                                "   inner join aircrafts acr on fl.aircraft_id = acr.id\n" +
                                "   inner join airlines als on acr.airline_id = als.id\n" +
                                "   inner join tariffs tf on als.id = tf.airline_id,\n" +
                                "   lateral(select (fl.default_price_for_kids + tf.price_for_kids) * :amountOfChildren + (fl.default_price_for_adults + tf.price_for_adults) * :amountOfAdults) as tab(min_price)\n" +
                                "         where (fl.departure_airport_id = :airportFromId\n" +
                                "            and fl.arrival_airport_id = :airportToId\n" +
                                "            and fl.departure_date = :dateFrom\n" +
                                "            and tf.tariff_type = :tariffType\n" +
                                "            and tf.has_baggage = :hasBaggage\n" +
                                "            and fl.departure_time >= :departureTimeFrom\n" +
                                "            and fl.arrival_time >= :arrivalTimeFrom\n" +
                                "            and extract(epoch from fl.arrival_time - fl.departure_time) / 3600 <= :flightDurationTimeUntilInHH\n" +
                                "            and (min_price <= :maxPrice))) as res\n" +
                                "   group by res.fl_id order by :orderBy limit :limit offset :offset"
                )
                .setParameter("airportFromId", airportFromId)
                .setParameter("airportToId", airportToId)
                .setParameter("dateFrom", dateFrom)
                .setParameter("amountOfAdults", amountOfAdults)
                .setParameter("amountOfChildren", amountOfChildren)
                .setParameter("tariffType", tariffType)
                .setParameter("hasBaggage", hasBaggage)
                .setParameter("departureTimeFrom", departureTimeFrom)
                .setParameter("arrivalTimeFrom", arrivalTimeFrom)
                .setParameter("flightDurationTimeUntilInHH", flightDurationTimeUntilInHH)
                .setParameter("maxPrice", maxPrice)
                .setParameter("orderBy", orderBy)
                .setParameter("limit", limit)
                .setParameter("offset", offset)
                .getResultList()
        );
    }
}
