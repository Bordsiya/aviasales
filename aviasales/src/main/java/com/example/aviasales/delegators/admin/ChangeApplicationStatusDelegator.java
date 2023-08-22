package com.example.aviasales.delegators.admin;

import com.example.aviasales.dto.requests.SetApplicationStatusRequestDTO;
import com.example.aviasales.entity.Application;
import com.example.aviasales.service.ApplicationService;
import com.example.aviasales.service.camunda.DelegateAuthCheckService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Named;
import java.time.format.DateTimeFormatter;

@Named
public class ChangeApplicationStatusDelegator implements JavaDelegate {
    private ApplicationService applicationService;
    private DelegateAuthCheckService delegateAuthCheckService;
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    @Autowired
    public ChangeApplicationStatusDelegator(ApplicationService applicationService,
                                            DelegateAuthCheckService delegateAuthCheckService) {
        this.applicationService = applicationService;
        this.delegateAuthCheckService = delegateAuthCheckService;
    }
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try {
            delegateAuthCheckService.checkAdminAuthority(execution);
            SetApplicationStatusRequestDTO setApplicationStatusRequestDTO = new SetApplicationStatusRequestDTO(
                    Long.parseLong(String.valueOf(execution.getVariable("applicationId"))),
                    String.valueOf(execution.getVariable("applicationStatus")),
                    String.valueOf(execution.getVariable("comment"))
            );
            Application application = applicationService.changeApplicationStatus(setApplicationStatusRequestDTO);

            execution.setVariable("applicationId", application.getApplicationId());
            execution.setVariable("userId", application.getUser().getUserId());
            execution.setVariable("email", application.getUser().getEmail());
            execution.setVariable("password", application.getUser().getPassword());
            execution.setVariable("role", application.getUser().getRole().name());
            execution.setVariable("applicationType", application.getApplicationType().name());
            execution.setVariable("applicationStatus", application.getApplicationStatus().name());
            execution.setVariable("payload", application.getPayload());
            execution.setVariable("publishDate", dateTimeFormatter.format(application.getPublishDate()));
            execution.setVariable("isArchived", application.getIsArchived());
        }
        catch (Throwable throwable) {
            execution.setVariable("error", throwable.getMessage());
            throw new BpmnError("CHANGE_APPLICATION_STATUS_ERROR", throwable.getMessage());
        }
    }
}
