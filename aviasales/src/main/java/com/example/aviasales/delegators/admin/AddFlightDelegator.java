package com.example.aviasales.delegators.admin;

import com.example.aviasales.dto.FlightDTO;
import com.example.aviasales.dto.requests.AddFlightsDTO;
import com.example.aviasales.entity.Flight;
import com.example.aviasales.service.FlightService;
import com.example.aviasales.service.camunda.DelegateAuthCheckService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Named;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

@Named
public class AddFlightDelegator implements JavaDelegate {
    private FlightService flightService;
    private DelegateAuthCheckService delegateAuthCheckService;
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private DateTimeFormatter dateTimeFormatterOutput = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private DateTimeFormatter timeFormatterOutput = DateTimeFormatter.ofPattern("hh:mm");

    @Autowired
    public AddFlightDelegator(FlightService flightService,
                              DelegateAuthCheckService delegateAuthCheckService) {
        this.flightService = flightService;
        this.delegateAuthCheckService = delegateAuthCheckService;
    }
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try {
            delegateAuthCheckService.checkAdminAuthority(execution);
            AddFlightsDTO addFlightsDTO = new AddFlightsDTO(Set.of(
                    new FlightDTO(
                            Long.parseLong(String.valueOf(execution.getVariable("departureAirportId"))),
                            Long.parseLong(String.valueOf(execution.getVariable("arrivalAirportId"))),
                            LocalDate.parse(String.valueOf(execution.getVariable("departureDate")), dateTimeFormatter),
                            LocalDate.parse(String.valueOf(execution.getVariable("arrivalDate")), dateTimeFormatter),
                            String.valueOf(execution.getVariable("departureTime")),
                            String.valueOf(execution.getVariable("arrivalTime")),
                            Long.parseLong(String.valueOf(execution.getVariable("defaultPriceForKids"))),
                            Long.parseLong(String.valueOf(execution.getVariable("defaultPriceForAdults"))),
                            Long.parseLong(String.valueOf(execution.getVariable("aircraftId")))
                    )
            ));
            Set<Flight> addedFlights = flightService.addFlights(addFlightsDTO);
            Flight flight = addedFlights.iterator().next();

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
            execution.setVariable("departureDate", dateTimeFormatterOutput.format(flight.getDepartureDate()));
            execution.setVariable("arrivalDate", dateTimeFormatterOutput.format(flight.getArrivalDate()));
            execution.setVariable("departureTime", timeFormatterOutput.format(flight.getDepartureTime()));
            execution.setVariable("arrivalTime", timeFormatterOutput.format(flight.getArrivalTime()));
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
            throw new BpmnError("ADD_FLIGHT_ERROR", throwable.getMessage());
        }
    }
}
