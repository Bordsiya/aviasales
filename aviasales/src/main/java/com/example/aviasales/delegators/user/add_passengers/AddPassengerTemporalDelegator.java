package com.example.aviasales.delegators.user.add_passengers;

import com.example.aviasales.dto.PassengerDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class AddPassengerTemporalDelegator implements JavaDelegate {
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private ObjectMapper objectMapper;
    @Autowired
    public AddPassengerTemporalDelegator(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try {
            PassengerDTO passengerDTO = new PassengerDTO(
                    String.valueOf(execution.getVariable("firstName")),
                    String.valueOf(execution.getVariable("lastName")),
                    String.valueOf(execution.getVariable("patronymic")),
                    String.valueOf(execution.getVariable("gender")),
                    String.valueOf(execution.getVariable("citizenship")),
                    Boolean.valueOf(String.valueOf(execution.getVariable("isKid"))),
                    String.valueOf(execution.getVariable("documentType")),
                    String.valueOf(execution.getVariable("documentNumber")),
                    LocalDate.parse(String.valueOf(execution.getVariable("expirationDate")), dateTimeFormatter),
                    Boolean.valueOf(String.valueOf(execution.getVariable("hasHearingDifficulties"))),
                    Boolean.valueOf(String.valueOf(execution.getVariable("hasVisionDifficulties"))),
                    Boolean.valueOf(String.valueOf(execution.getVariable("requiredWheelchair"))),
                    Long.parseLong(String.valueOf(execution.getVariable("tariffId")))
            );
            Set<PassengerDTO> passengers = objectMapper.readValue(
                    String.valueOf(execution.getVariable("passengers")),
                    new TypeReference<Set<PassengerDTO>>(){}
            );
            passengers.add(passengerDTO);
            execution.setVariable("passengers", objectMapper.writeValueAsString(passengers));
        }
        catch (Throwable throwable) {
            execution.setVariable("error", throwable.getMessage());
            throw new BpmnError("ADD_PASSENGER_ERROR", throwable.getMessage());
        }
    }
}
