package com.example.aviasales.delegators.user;

import com.example.aviasales.dto.requests.SearchRequestDTO;
import com.example.aviasales.dto.responses.search_response.SearchResponseDTO;
import com.example.aviasales.service.FlightService;
import com.example.aviasales.service.camunda.DelegateAuthCheckService;
import com.example.aviasales.util.mappers.SearchRequestMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
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
    private ObjectMapper objectMapper;

    @Autowired
    public SearchFlightsDelegator(FlightService flightService,
                                  SearchRequestMapper searchRequestMapper,
                                  DelegateAuthCheckService delegateAuthCheckService,
                                  ObjectMapper objectMapper) {
        this.flightService = flightService;
        this.searchRequestMapper = searchRequestMapper;
        this.delegateAuthCheckService = delegateAuthCheckService;
        this.objectMapper = objectMapper;
    }
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try {
            delegateAuthCheckService.checkCustomerAuthority(execution);
            SearchRequestDTO searchRequestDTO = new SearchRequestDTO(
                    Long.parseLong(String.valueOf(execution.getVariable("airportFromId"))),
                    Long.parseLong(String.valueOf(execution.getVariable("airportToId"))),
                    LocalDate.parse(String.valueOf(execution.getVariable("dateFrom"))),
                    String.valueOf(execution.getVariable("dateBack")).isBlank() ? null
                            : LocalDate.parse(String.valueOf(execution.getVariable("dateBack"))),
                    Long.parseLong(String.valueOf(execution.getVariable("amountOfAdults"))),
                    Long.parseLong(String.valueOf(execution.getVariable("amountOfChildren"))),
                    String.valueOf(execution.getVariable("tariffType")),
                    String.valueOf(execution.getVariable("hasBaggage")).isBlank() ? null :
                            Boolean.valueOf(String.valueOf(execution.getVariable("hasBaggage"))),
                    String.valueOf(execution.getVariable("departureTimeFrom")).isBlank() ? null :
                            String.valueOf(execution.getVariable("departureTimeFrom")),
                    String.valueOf(execution.getVariable("arrivalTimeFrom")).isBlank() ? null :
                            String.valueOf(execution.getVariable("arrivalTimeFrom")),
                    String.valueOf(execution.getVariable("flightDurationTimeUntilInHH")).isBlank() ? null :
                            Long.parseLong(String.valueOf(execution.getVariable("flightDurationTimeUntilInHH"))),
                    String.valueOf(execution.getVariable("maxPrice")).isBlank() ? null :
                            Long.parseLong(String.valueOf(execution.getVariable("maxPrice"))),
                    String.valueOf(execution.getVariable("sortingAlgorithm")).equals("null")? null :
                            String.valueOf(execution.getVariable("sortingAlgorithm")),
                    String.valueOf(execution.getVariable("pageNumber")).isBlank() ? null :
                            Integer.parseInt(String.valueOf(execution.getVariable("pageNumber"))),
                    String.valueOf(execution.getVariable("pageSize")).isBlank() ? null :
                            Integer.parseInt(String.valueOf(execution.getVariable("pageSize")))
            );
            List<SearchResponseDTO> searchedFlights = flightService
                    .getFlightsFiltered(searchRequestMapper.fromDTO(searchRequestDTO));
            execution.setVariable("result", objectMapper.writeValueAsString(searchedFlights));
        }
        catch (Throwable throwable) {
            execution.setVariable("error", throwable.getMessage());
            throw new BpmnError("SEARCH_FLIGHTS_ERROR", throwable.getMessage());
        }
    }
}
