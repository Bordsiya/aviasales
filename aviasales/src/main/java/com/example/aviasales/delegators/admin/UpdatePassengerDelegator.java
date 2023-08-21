package com.example.aviasales.delegators.admin;

import com.example.aviasales.dto.PassengerDTO;
import com.example.aviasales.entity.Passenger;
import com.example.aviasales.service.PassengerService;
import com.example.aviasales.service.camunda.DelegateAuthCheckService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Named;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Named
public class UpdatePassengerDelegator implements JavaDelegate {
    private PassengerService passengerService;
    private DelegateAuthCheckService delegateAuthCheckService;
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    @Autowired
    public UpdatePassengerDelegator(PassengerService passengerService,
                                    DelegateAuthCheckService delegateAuthCheckService) {
        this.passengerService = passengerService;
        this.delegateAuthCheckService = delegateAuthCheckService;
    }
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try {
            delegateAuthCheckService.checkAdminAuthority(execution);
            Long passengerId = Long.parseLong(String.valueOf(execution.getVariable("passengerId")));
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
            Passenger updatedPassenger = passengerService.updatePassenger(passengerId, passengerDTO);
            execution.setVariable("firstName", updatedPassenger.getFirstName());
            execution.setVariable("lastName", updatedPassenger.getLastName());
            execution.setVariable("patronymic", updatedPassenger.getPatronymic());
            execution.setVariable("gender", updatedPassenger.getGender().name());
            execution.setVariable("citizenship", updatedPassenger.getCitizenship());
            execution.setVariable("isKid", updatedPassenger.getIsKid());
            execution.setVariable("documentType", updatedPassenger.getDocumentType().name());
            execution.setVariable("documentNumber", updatedPassenger.getDocumentNumber());
            execution.setVariable("expirationDate", updatedPassenger.getExpirationDate());
            execution.setVariable("hasHearingDifficulties", updatedPassenger.getHasHearingDifficulties());
            execution.setVariable("hasVisionDifficulties", updatedPassenger.getHasVisionDifficulties());
            execution.setVariable("requiredWheelchair", updatedPassenger.getRequiredWheelchair());
            execution.setVariable("tariffId", updatedPassenger.getTariff());
        }
        catch (Throwable throwable) {
            execution.setVariable("error", throwable.getMessage());
            throw new BpmnError("UPDATE_PASSENGER_ERROR", throwable.getMessage());
        }
    }
}
