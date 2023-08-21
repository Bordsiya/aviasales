package com.example.aviasales.delegators.admin;

import com.example.aviasales.dto.requests.DeletePassengersDTO;
import com.example.aviasales.service.PassengerService;
import com.example.aviasales.service.camunda.DelegateAuthCheckService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Named;
import java.util.List;

@Named
public class DeletePassengerDelegator implements JavaDelegate {
    private PassengerService passengerService;
    private DelegateAuthCheckService delegateAuthCheckService;

    @Autowired
    public DeletePassengerDelegator(PassengerService passengerService,
                                    DelegateAuthCheckService delegateAuthCheckService) {
        this.passengerService = passengerService;
        this.delegateAuthCheckService = delegateAuthCheckService;
    }
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try {
            delegateAuthCheckService.checkAdminAuthority(execution);
            Long passengerId = Long.parseLong(String.valueOf(execution.getVariable("passengerId")));
            passengerService.deletePassengers(new DeletePassengersDTO(List.of(passengerId)));
            execution.setVariable("passengerId", passengerId);
        }
        catch (Throwable throwable) {
            execution.setVariable("error", throwable.getMessage());
            throw new BpmnError("DELETE_PASSENGER_ERROR", throwable.getMessage());
        }
    }
}
