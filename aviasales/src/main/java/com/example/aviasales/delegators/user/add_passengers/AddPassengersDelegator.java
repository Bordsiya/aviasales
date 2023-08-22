package com.example.aviasales.delegators.user.add_passengers;

import com.example.aviasales.dto.PassengerDTO;
import com.example.aviasales.dto.requests.AddPassengersDTO;
import com.example.aviasales.service.PassengerService;
import com.example.aviasales.service.camunda.DelegateAuthCheckService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Named
public class AddPassengersDelegator implements JavaDelegate {
    private PassengerService passengerService;
    private DelegateAuthCheckService delegateAuthCheckService;
    private ObjectMapper objectMapper;
    @Autowired
    public AddPassengersDelegator(PassengerService passengerService,
                                  DelegateAuthCheckService delegateAuthCheckService,
                                  ObjectMapper objectMapper) {
        this.passengerService = passengerService;
        this.delegateAuthCheckService = delegateAuthCheckService;
        this.objectMapper = objectMapper;
    }
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try {
            delegateAuthCheckService.checkCustomerAuthority(execution);
            Set<PassengerDTO> passengers = objectMapper.readValue(
                    String.valueOf(execution.getVariable("passengers")),
                    new TypeReference<Set<PassengerDTO>>(){}
            );
            AddPassengersDTO addPassengersDTO = new AddPassengersDTO(
                    passengers,
                    String.valueOf(execution.getVariable("phoneNumber")),
                    String.valueOf(execution.getVariable("email")),
                    Long.parseLong(String.valueOf(execution.getVariable("flightId")))
            );
            execution.setVariable("result", passengerService.addPassengers(addPassengersDTO).toString());
        }
        catch (Throwable throwable) {
            execution.setVariable("error", throwable.getMessage());
            throw new BpmnError("ADD_PASSENGERS_ERROR", throwable.getMessage());
        }
    }
}
