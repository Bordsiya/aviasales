package com.example.aviasales.delegators.admin;

import com.example.aviasales.dto.requests.DeleteFlightsRequest;
import com.example.aviasales.service.FlightService;
import com.example.aviasales.service.camunda.DelegateAuthCheckService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Named;
import java.util.List;

@Named
public class DeleteFlightDelegator implements JavaDelegate {
    private FlightService flightService;
    private DelegateAuthCheckService delegateAuthCheckService;

    @Autowired
    public DeleteFlightDelegator(FlightService flightService,
                                 DelegateAuthCheckService delegateAuthCheckService) {
        this.flightService = flightService;
        this.delegateAuthCheckService = delegateAuthCheckService;
    }
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try {
            delegateAuthCheckService.checkAdminAuthority(execution);
            Long flightId = Long.parseLong(String.valueOf(execution.getVariable("flightId")));
            flightService.deleteFlights(new DeleteFlightsRequest(List.of(flightId)));
            execution.setVariable("flightId", flightId);
        }
        catch (Throwable throwable) {
            execution.setVariable("error", throwable.getMessage());
            throw new BpmnError("DELETE_FLIGHT_ERROR", throwable.getMessage());
        }
    }
}
