package com.example.aviasales.delegators.user;

import com.example.aviasales.entity.Reservation;
import com.example.aviasales.service.ReservationService;
import com.example.aviasales.service.camunda.DelegateAuthCheckService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Named;

@Named
public class GetReservationDelegator implements JavaDelegate {
    private ReservationService reservationService;
    private DelegateAuthCheckService delegateAuthCheckService;

    @Autowired
    public GetReservationDelegator(ReservationService reservationService,
                                   DelegateAuthCheckService delegateAuthCheckService) {
        this.reservationService = reservationService;
        this.delegateAuthCheckService = delegateAuthCheckService;
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try {
            delegateAuthCheckService.checkCustomerAuthority(execution);
            Long reservationId = Long.parseLong(String.valueOf(execution.getVariable("reservationId")));
            Reservation reservation = reservationService.getReservationById(reservationId);
            execution.setVariable("reservationId", reservation.getReservationId());
            execution.setVariable("reservationCode", reservation.getReservationCode());
            execution.setVariable("time", reservation.getTime());
            execution.setVariable("phoneNumber", reservation.getPhoneNumber());
            execution.setVariable("email", reservation.getEmail());
        }
        catch (Throwable throwable) {
            execution.setVariable("error", throwable.getMessage());
            throw new BpmnError("GET_RESERVATION_ERROR", throwable.getMessage());
        }
    }
}
