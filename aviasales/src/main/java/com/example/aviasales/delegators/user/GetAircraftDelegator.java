package com.example.aviasales.delegators.user;

import com.example.aviasales.entity.Aircraft;
import com.example.aviasales.service.AircraftService;
import com.example.aviasales.service.camunda.DelegateAuthCheckService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Named;

@Named
public class GetAircraftDelegator implements JavaDelegate {
    private AircraftService aircraftService;
    private DelegateAuthCheckService delegateAuthCheckService;

    @Autowired
    public GetAircraftDelegator(AircraftService aircraftService,
                                DelegateAuthCheckService delegateAuthCheckService) {
        this.aircraftService = aircraftService;
        this.delegateAuthCheckService = delegateAuthCheckService;
    }
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try {
            delegateAuthCheckService.checkCustomerAuthority(execution);
            Long aircraftId = Long.parseLong(String.valueOf(execution.getVariable("aircraftId")));
            Aircraft aircraft = aircraftService.getAircraftById(aircraftId);
            execution.setVariable("aircraftId", aircraft.getAircraftId());
            execution.setVariable("manufacturer", aircraft.getManufacturer());
            execution.setVariable("model", aircraft.getModel());
            execution.setVariable("numberOfSeats", aircraft.getNumberOfSeats());
            execution.setVariable("airlineId", aircraft.getAirline().getAirlineId());
            execution.setVariable("airlineName", aircraft.getAirline().getAirlineName());
        }
        catch (Throwable throwable) {
            execution.setVariable("error", throwable.getMessage());
            throw new BpmnError("GET_AIRCRAFT_ERROR", throwable.getMessage());
        }
    }
}
