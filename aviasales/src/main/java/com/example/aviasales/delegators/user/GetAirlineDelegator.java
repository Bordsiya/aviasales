package com.example.aviasales.delegators.user;

import com.example.aviasales.entity.Airline;
import com.example.aviasales.service.AirlineService;
import com.example.aviasales.service.camunda.DelegateAuthCheckService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Named;

@Named
public class GetAirlineDelegator implements JavaDelegate {
    private AirlineService airlineService;
    private DelegateAuthCheckService delegateAuthCheckService;
    @Autowired
    public GetAirlineDelegator(AirlineService airlineService,
                               DelegateAuthCheckService delegateAuthCheckService) {
        this.airlineService = airlineService;
        this.delegateAuthCheckService = delegateAuthCheckService;
    }
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try {
            delegateAuthCheckService.checkCustomerAuthority(execution);
            Long airlineId = Long.parseLong(String.valueOf(execution.getVariable("airlineId")));
            Airline airline = airlineService.getAirlineById(airlineId);
            execution.setVariable("airlineId", airline.getAirlineId());
            execution.setVariable("airlineName", airline.getAirlineName());
        }
        catch (Throwable throwable) {
            execution.setVariable("error", throwable.getMessage());
            throw new BpmnError("GET_AIRLINE_ERROR", throwable.getMessage());
        }
    }
}
