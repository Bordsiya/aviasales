package com.example.aviasales.delegators.user;

import com.example.aviasales.dto.PassengerDTO;
import com.example.aviasales.entity.Application;
import com.example.aviasales.service.ApplicationService;
import com.example.aviasales.service.camunda.DelegateAuthCheckService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.inject.Named;
import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Named
public class CreateUpdatePassengerApplicationDelegator implements JavaDelegate {
    private ApplicationService applicationService;
    private DelegateAuthCheckService delegateAuthCheckService;
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private DateTimeFormatter dateTimeFormatterOutput = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    @Autowired
    public CreateUpdatePassengerApplicationDelegator(ApplicationService applicationService,
                                                     DelegateAuthCheckService delegateAuthCheckService) {
        this.applicationService = applicationService;
        this.delegateAuthCheckService = delegateAuthCheckService;
    }
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try {
            delegateAuthCheckService.checkCustomerAuthority(execution);
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
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            Application application = applicationService.addApplicationUpdatePassenger(passengerId, passengerDTO, email);
            execution.setVariable("applicationId", application.getApplicationId());
            execution.setVariable("userId", application.getUser().getUserId());
            execution.setVariable("email", application.getUser().getEmail());
            execution.setVariable("password", application.getUser().getPassword());
            execution.setVariable("role", application.getUser().getRole().name());
            execution.setVariable("applicationType", application.getApplicationType().name());
            execution.setVariable("applicationStatus", application.getApplicationStatus().name());
            execution.setVariable("payload", application.getPayload());
            execution.setVariable("publishDate", dateTimeFormatterOutput.format(application.getPublishDate()));
            execution.setVariable("isArchived", application.getIsArchived());
        }
        catch (Throwable throwable) {
            execution.setVariable("error", throwable.getMessage());
            throw new BpmnError("CREATE_UPDATE_PASSENGER_APPLICATION_ERROR", throwable.getMessage());
        }
    }
}
