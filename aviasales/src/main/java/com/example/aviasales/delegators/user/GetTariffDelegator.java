package com.example.aviasales.delegators.user;

import com.example.aviasales.entity.Tariff;
import com.example.aviasales.service.TariffService;
import com.example.aviasales.service.camunda.DelegateAuthCheckService;
import com.example.aviasales.util.enums.TariffType;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Named;

@Named
public class GetTariffDelegator implements JavaDelegate {
    private TariffService tariffService;
    private DelegateAuthCheckService delegateAuthCheckService;

    @Autowired
    public GetTariffDelegator(TariffService tariffService,
                              DelegateAuthCheckService delegateAuthCheckService) {
        this.tariffService = tariffService;
        this.delegateAuthCheckService = delegateAuthCheckService;
    }
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try {
            delegateAuthCheckService.checkCustomerAuthority(execution);
            Long tariffId = Long.parseLong(String.valueOf(execution.getVariable("tariffId")));
            Tariff tariff = tariffService.getTariffById(tariffId);
            execution.setVariable("tariffId", tariff.getTariffId());
            execution.setVariable("tariffType", tariff.getTariffType().name());
            execution.setVariable("tariffName", tariff.getTariffName());
            execution.setVariable("airlineId", tariff.getAirline().getAirlineId());
            execution.setVariable("airlineName", tariff.getAirline().getAirlineName());
            execution.setVariable("priceForKids", tariff.getPriceForKids());
            execution.setVariable("priceForAdults", tariff.getPriceForAdults());
            execution.setVariable("hasExchange", tariff.getHasExchange());
            execution.setVariable("hasRefund", tariff.getHasRefund());
            execution.setVariable("hasBaggage", tariff.getHasBaggage());
            execution.setVariable("amountOfBaggage", tariff.getAmountOfBaggage());
            execution.setVariable("weightPerBaggageInKg", tariff.getWeightPerBaggageInKg());
            execution.setVariable("sumOfBaggageSidesInCm", tariff.getSumOfBaggageSidesInCm());
            execution.setVariable("amountOfHandBaggage", tariff.getAmountOfHandBaggage());
            execution.setVariable("weightPerHandBaggageInKg", tariff.getWeightPerHandBaggageInKg());
            execution.setVariable("heightPerHandBaggageInCm", tariff.getHeightPerHandBaggageInCm());
            execution.setVariable("widthPerHandBaggageInCm", tariff.getWidthPerHandBaggageInCm());
            execution.setVariable("depthPerHandBaggageInCm", tariff.getDepthPerHandBaggageInCm());
        }
        catch (Throwable throwable) {
            execution.setVariable("error", throwable.getMessage());
            throw new BpmnError("GET_TARIFF_ERROR", throwable.getMessage());
        }
    }
}
