package com.example.aviasales.delegators.user;

import com.example.aviasales.entity.Airport;
import com.example.aviasales.service.AirportService;
import com.example.aviasales.service.camunda.DelegateAuthCheckService;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Named;

@Named
@Slf4j
public class GetAirportDelegator implements JavaDelegate {
    private AirportService airportService;
    private DelegateAuthCheckService delegateAuthCheckService;

    @Autowired
    public GetAirportDelegator(AirportService airportService,
                               DelegateAuthCheckService delegateAuthCheckService) {
        this.airportService = airportService;
        this.delegateAuthCheckService = delegateAuthCheckService;
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try {
            delegateAuthCheckService.checkCustomerAuthority(execution);
            Long airportId = Long.parseLong(String.valueOf(execution.getVariable("airportId")));
            Airport airport = airportService.getAirportById(airportId);
            execution.setVariable("airportId", airport.getAirportId());
            execution.setVariable("airportCode", airport.getAirportCode());
            execution.setVariable("airportName", airport.getAirportName());
            execution.setVariable("city", airport.getCity());
            execution.setVariable("state", airport.getState());
            execution.setVariable("country", airport.getCountry());
        }
        catch (Throwable throwable) {
            execution.setVariable("error", throwable.getMessage());
            throw new BpmnError("GET_AIRPORT_ERROR", throwable.getMessage());
        }
    }
}
