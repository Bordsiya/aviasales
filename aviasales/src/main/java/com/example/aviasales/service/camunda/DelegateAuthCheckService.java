package com.example.aviasales.service.camunda;

import com.example.aviasales.util.camunda.CamundaHttpBasicFilter;
import com.example.aviasales.util.camunda.CamundaHttpBasicProvider;
import com.example.aviasales.util.enums.RoleType;
import org.camunda.bpm.engine.AuthorizationException;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class DelegateAuthCheckService {
    private CamundaHttpBasicFilter camundaHttpBasicFilter;
    @Autowired
    public DelegateAuthCheckService(CamundaHttpBasicFilter camundaHttpBasicFilter) {
        this.camundaHttpBasicFilter = camundaHttpBasicFilter;
    }

    public void checkAdminAuthority(DelegateExecution execution) throws IllegalAccessException, AuthorizationException {
        checkAuthority(execution, List.of("ADMIN"));
    }
    public void checkCustomerAuthority(DelegateExecution execution) throws IllegalAccessException, AuthorizationException {
        checkAuthority(execution, List.of("ADMIN", "CUSTOMER"));
    }
    public void checkAuthority(DelegateExecution execution, List<String> roles) throws IllegalAccessException, AuthorizationException {
        String userId = execution.getProcessEngineServices().getIdentityService().getCurrentAuthentication().getUserId();
        String token = TokenService.getUserToken(userId);
        if (token == null) {
            throw new AuthorizationException("User is not authorized");
        }
        camundaHttpBasicFilter.doFilter(token);
        List<String> userRoles = SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        if (Collections.disjoint(userRoles, roles)) {
            throw new IllegalAccessException("You don't have permissions for this process");
        }
    }
}
