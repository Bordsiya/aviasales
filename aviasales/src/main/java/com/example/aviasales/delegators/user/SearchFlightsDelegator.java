package com.example.aviasales.delegators.user;

import com.example.aviasales.dto.requests.SearchRequestDTO;
import com.example.aviasales.dto.responses.search_response.SearchResponseDTO;
import com.example.aviasales.service.FlightService;
import com.example.aviasales.service.camunda.DelegateAuthCheckService;
import com.example.aviasales.util.mappers.SearchRequestMapper;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Named;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Named
public class SearchFlightsDelegator implements JavaDelegate {
    private FlightService flightService;
    private SearchRequestMapper searchRequestMapper;
    private DelegateAuthCheckService delegateAuthCheckService;
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    @Autowired
    public SearchFlightsDelegator(FlightService flightService,
                                  SearchRequestMapper searchRequestMapper,
                                  DelegateAuthCheckService delegateAuthCheckService) {
        this.flightService = flightService;
        this.searchRequestMapper = searchRequestMapper;
        this.delegateAuthCheckService = delegateAuthCheckService;
    }
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try {
            delegateAuthCheckService.checkCustomerAuthority(execution);
            SearchRequestDTO searchRequestDTO = new SearchRequestDTO(
                    Long.parseLong(String.valueOf(execution.getVariable("airportFromId"))),
                    Long.parseLong(String.valueOf(execution.getVariable("airportToId"))),
                    LocalDate.parse(String.valueOf(execution.getVariable("dateFrom")), dateTimeFormatter),
                    LocalDate.parse(String.valueOf(execution.getVariable("dateBack")), dateTimeFormatter),
                    Long.parseLong(String.valueOf(execution.getVariable("amountOfAdults"))),
                    Long.parseLong(String.valueOf(execution.getVariable("amountOfChildren"))),
                    String.valueOf(execution.getVariable("tariffType")),
                    Boolean.valueOf(String.valueOf(execution.getVariable("hasBaggage"))),
                    String.valueOf(execution.getVariable("departureTimeFrom")),
                    String.valueOf(execution.getVariable("arrivalTimeFrom")),
                    Long.parseLong(String.valueOf(execution.getVariable("flightDurationTimeUntilInHH"))),
                    Long.parseLong(String.valueOf(execution.getVariable("maxPrice"))),
                    String.valueOf(execution.getVariable("sortingAlgorithm")),
                    Integer.parseInt(String.valueOf(execution.getVariable("pageNumber"))),
                    Integer.parseInt(String.valueOf(execution.getVariable("pageSize")))
            );
            List<SearchResponseDTO> searchedFlights = flightService
                    .getFlightsFiltered(searchRequestMapper.fromDTO(searchRequestDTO));
            execution.setVariable("result", searchedFlights);
        }
        catch (Throwable throwable) {
            execution.setVariable("error", throwable.getMessage());
            throw new BpmnError("SEARCH_FLIGHTS_ERROR", throwable.getMessage());
        }
    }
}
