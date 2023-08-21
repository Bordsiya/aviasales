package com.example.aviasales.delegators.user;

import com.example.aviasales.entity.Flight;
import com.example.aviasales.service.FlightService;
import com.example.aviasales.service.camunda.DelegateAuthCheckService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Named;

@Named
public class GetFlightDelegator implements JavaDelegate {
    private FlightService flightService;
    private DelegateAuthCheckService delegateAuthCheckService;
    @Autowired
    public GetFlightDelegator(FlightService flightService,
                              DelegateAuthCheckService delegateAuthCheckService) {
        this.flightService = flightService;
        this.delegateAuthCheckService = delegateAuthCheckService;
    }
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try {
            delegateAuthCheckService.checkCustomerAuthority(execution);
            Long flightId = Long.parseLong(String.valueOf(execution.getVariable("flightId")));
            Flight flight = flightService.getFlightById(flightId);
            execution.setVariable("flightId", flight.getFlightId());
            execution.setVariable("departureAirportId", flight.getDepartureAirport().getAirportId());
            execution.setVariable("departureAirportCode", flight.getDepartureAirport().getAirportCode());
            execution.setVariable("departureAirportName", flight.getDepartureAirport().getAirportName());
            execution.setVariable("departureCity", flight.getDepartureAirport().getCity());
            execution.setVariable("departureState", flight.getDepartureAirport().getState());
            execution.setVariable("departureCountry", flight.getDepartureAirport().getCountry());
            execution.setVariable("arrivalAirportId", flight.getArrivalAirport().getAirportId());
            execution.setVariable("arrivalAirportCode", flight.getArrivalAirport().getAirportCode());
            execution.setVariable("arrivalAirportName", flight.getArrivalAirport().getAirportName());
            execution.setVariable("arrivalCity", flight.getArrivalAirport().getCity());
            execution.setVariable("arrivalState", flight.getArrivalAirport().getState());
            execution.setVariable("arrivalCountry", flight.getArrivalAirport().getCountry());
            execution.setVariable("departureDate", flight.getDepartureDate());
            execution.setVariable("arrivalDate", flight.getArrivalDate());
            execution.setVariable("departureTime", flight.getDepartureTime());
            execution.setVariable("arrivalTime", flight.getArrivalTime());
            execution.setVariable("defaultPriceForKids", flight.getDefaultPriceForKids());
            execution.setVariable("defaultPriceForAdults", flight.getDefaultPriceForAdults());
            execution.setVariable("aircraftId", flight.getAircraft().getAircraftId());
            execution.setVariable("manufacturer", flight.getAircraft().getManufacturer());
            execution.setVariable("model", flight.getAircraft().getModel());
            execution.setVariable("numberOfSeats", flight.getAircraft().getNumberOfSeats());
            execution.setVariable("airlineId", flight.getAircraft().getAirline().getAirlineId());
            execution.setVariable("airlineName", flight.getAircraft().getAirline().getAirlineName());
        }
        catch (Throwable throwable) {
            execution.setVariable("error", throwable.getMessage());
            throw new BpmnError("GET_FLIGHT_ERROR", throwable.getMessage());
        }
    }
}
