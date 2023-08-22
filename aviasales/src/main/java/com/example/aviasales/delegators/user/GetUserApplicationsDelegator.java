package com.example.aviasales.delegators.user;

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
import java.util.Set;

@Named
public class GetUserApplicationsDelegator implements JavaDelegate {
    private ApplicationService applicationService;
    private DelegateAuthCheckService delegateAuthCheckService;
    @Autowired
    public GetUserApplicationsDelegator(ApplicationService applicationService,
                                        DelegateAuthCheckService delegateAuthCheckService) {
        this.applicationService = applicationService;
        this.delegateAuthCheckService = delegateAuthCheckService;
    }
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try {
            delegateAuthCheckService.checkCustomerAuthority(execution);
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            Set<Application> applicationSet = applicationService.getUserApplications(email);
            execution.setVariable("result", applicationSet.toString());
        }
        catch (Throwable throwable) {
            execution.setVariable("error", throwable.getMessage());
            throw new BpmnError("GET_USER_APPLICATIONS_ERROR", throwable.getMessage());
        }
    }
}
