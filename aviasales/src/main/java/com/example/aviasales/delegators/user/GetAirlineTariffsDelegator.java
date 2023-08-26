package com.example.aviasales.delegators.user;

import com.example.aviasales.entity.Tariff;
import com.example.aviasales.service.AirlineService;
import com.example.aviasales.service.camunda.DelegateAuthCheckService;
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
public class GetAirlineTariffsDelegator implements JavaDelegate {
    private AirlineService airlineService;
    private DelegateAuthCheckService delegateAuthCheckService;
    private ObjectMapper objectMapper;
    @Autowired
    public GetAirlineTariffsDelegator(AirlineService airlineService,
                                      DelegateAuthCheckService delegateAuthCheckService,
                                      ObjectMapper objectMapper) {
        this.airlineService = airlineService;
        this.delegateAuthCheckService = delegateAuthCheckService;
        this.objectMapper = objectMapper;
    }
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try {
            delegateAuthCheckService.checkCustomerAuthority(execution);
            Long airlineId = Long.parseLong(String.valueOf(execution.getVariable("airlineId")));
            Set<Tariff> tariffs = airlineService.getAirlineTariffsByAirlineId(airlineId);
            List<Tariff> tariffList = new ArrayList<>(tariffs);
            execution.setVariable("result", objectMapper.writeValueAsString(tariffList));
        }
        catch (Throwable throwable) {
            execution.setVariable("error", throwable.getMessage());
            throw new BpmnError("GET_AIRLINE_TARIFFS_ERROR", throwable.getMessage());
        }
    }
}
