package com.example.aviasales.delegators.admin;

import com.example.aviasales.entity.Application;
import com.example.aviasales.service.ApplicationService;
import com.example.aviasales.service.camunda.DelegateAuthCheckService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Named;

@Named
public class GetApplicationDelegator implements JavaDelegate {
    private ApplicationService applicationService;
    private DelegateAuthCheckService delegateAuthCheckService;
    @Autowired
    public GetApplicationDelegator(ApplicationService applicationService,
                                   DelegateAuthCheckService delegateAuthCheckService) {
        this.applicationService = applicationService;
        this.delegateAuthCheckService = delegateAuthCheckService;
    }
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try {
            delegateAuthCheckService.checkAdminAuthority(execution);
            Long applicationId = Long.parseLong(String.valueOf("applicationId"));
            Application application = applicationService.getApplicationById(applicationId);
            execution.setVariable("applicationId", application.getApplicationId());
            execution.setVariable("userId", application.getUser().getUserId());
            execution.setVariable("email", application.getUser().getEmail());
            execution.setVariable("password", application.getUser().getPassword());
            execution.setVariable("role", application.getUser().getRole().name());
            execution.setVariable("applicationType", application.getApplicationType().name());
            execution.setVariable("payload", application.getPayload());
            execution.setVariable("publishDate", application.getPublishDate());
            execution.setVariable("isArchived", application.getIsArchived());
        }
        catch (Throwable throwable) {
            execution.setVariable("error", throwable.getMessage());
            throw new BpmnError("GET_APPLICATION_ERROR", throwable.getMessage());
        }
    }
}
