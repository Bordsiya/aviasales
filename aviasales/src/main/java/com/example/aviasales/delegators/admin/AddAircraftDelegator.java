package com.example.aviasales.delegators.admin;

import com.example.aviasales.dto.AircraftDTO;
import com.example.aviasales.dto.requests.AddAircraftsDTO;
import com.example.aviasales.entity.Aircraft;
import com.example.aviasales.service.AircraftService;
import com.example.aviasales.service.camunda.DelegateAuthCheckService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Named;
import java.util.List;
import java.util.Set;

@Named
public class AddAircraftDelegator implements JavaDelegate {
    private AircraftService aircraftService;
    private DelegateAuthCheckService delegateAuthCheckService;

    @Autowired
    public AddAircraftDelegator(AircraftService aircraftService,
                                DelegateAuthCheckService delegateAuthCheckService) {
        this.aircraftService = aircraftService;
        this.delegateAuthCheckService = delegateAuthCheckService;
    }
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try {
            delegateAuthCheckService.checkAdminAuthority(execution);
            AircraftDTO aircraftDTO = new AircraftDTO(
                    String.valueOf(execution.getVariable("manufacturer")),
                    String.valueOf(execution.getVariable("model")),
                    Integer.parseInt(String.valueOf(execution.getVariable("numberOfSeats"))),
                    Long.parseLong(String.valueOf(execution.getVariable("airlineId")))
            );
            Set<Aircraft> aircrafts = aircraftService.addAircrafts(new AddAircraftsDTO(List.of(aircraftDTO)));
            Aircraft addedAircraft = aircrafts.iterator().next();

            execution.setVariable("aircraftId", addedAircraft.getAircraftId());
            execution.setVariable("manufacturer", addedAircraft.getManufacturer());
            execution.setVariable("model", addedAircraft.getModel());
            execution.setVariable("numberOfSeats", addedAircraft.getNumberOfSeats());
            execution.setVariable("airlineId", addedAircraft.getAirline().getAirlineId());
            execution.setVariable("airlineName", addedAircraft.getAirline().getAirlineName());
        }
        catch (Throwable throwable) {
            execution.setVariable("error", throwable.getMessage());
            throw new BpmnError("ADD_AIRCRAFT_ERROR", throwable.getMessage());
        }
    }
}
