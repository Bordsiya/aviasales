package com.example.aviasales.delegators.admin;

import com.example.aviasales.dto.AirlineDTO;
import com.example.aviasales.dto.requests.AddAirlinesDTO;
import com.example.aviasales.entity.Airline;
import com.example.aviasales.service.AirlineService;
import com.example.aviasales.service.camunda.DelegateAuthCheckService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Named;
import java.util.Set;

@Named
public class AddAirlineDelegator implements JavaDelegate {
    private AirlineService airlineService;
    private DelegateAuthCheckService delegateAuthCheckService;
    @Autowired
    public AddAirlineDelegator(AirlineService airlineService,
                               DelegateAuthCheckService delegateAuthCheckService) {
        this.airlineService = airlineService;
        this.delegateAuthCheckService = delegateAuthCheckService;
    }
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try {
            delegateAuthCheckService.checkAdminAuthority(execution);
            AddAirlinesDTO addAirlinesDTO = new AddAirlinesDTO(Set.of(
                    new AirlineDTO(
                            String.valueOf(execution.getVariable("airlineName"))
                    )
            ));
            Set<Airline> addedAirlines = airlineService.addAirlines(addAirlinesDTO);
            Airline airline = addedAirlines.iterator().next();

            execution.setVariable("airlineId", airline.getAirlineId());
            execution.setVariable("airlineName", airline.getAirlineName());
        }
        catch (Throwable throwable) {
            execution.setVariable("error", throwable.getMessage());
            throw new BpmnError("ADD_AIRLINE_ERROR", throwable.getMessage());
        }
    }
}
