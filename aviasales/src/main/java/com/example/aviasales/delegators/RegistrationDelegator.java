package com.example.aviasales.delegators;

import com.example.aviasales.dto.UserDTO;
import com.example.aviasales.entity.User;
import com.example.aviasales.service.AuthService;
import com.example.aviasales.service.camunda.DelegateAuthCheckService;
import com.example.aviasales.service.camunda.TokenService;
import com.example.aviasales.util.camunda.CamundaHttpBasicProvider;
import com.example.aviasales.util.enums.RoleType;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Named;

@Slf4j
@Named
public class RegistrationDelegator implements JavaDelegate {
    private AuthService authService;
    private CamundaHttpBasicProvider camundaHttpBasicProvider;
    private DelegateAuthCheckService delegateAuthCheckService;

    @Autowired
    public RegistrationDelegator(AuthService authService,
                                 CamundaHttpBasicProvider camundaHttpBasicProvider,
                                 DelegateAuthCheckService delegateAuthCheckService) {
        this.authService = authService;
        this.camundaHttpBasicProvider = camundaHttpBasicProvider;
        this.delegateAuthCheckService = delegateAuthCheckService;
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String userId = execution.getProcessEngineServices().getIdentityService().getCurrentAuthentication().getUserId();
        try {
            UserDTO userDTO = new UserDTO(
                    (String) execution.getVariable("email"),
                    (String) execution.getVariable("password")
            );
            String token = camundaHttpBasicProvider.encodeUsernamePassword(userDTO.getEmail(), userDTO.getPassword());
            log.error(token);

            Boolean isAdmin = (Boolean) execution.getVariable("isAdmin");
            User user = null;
            if (isAdmin) {
                delegateAuthCheckService.checkAdminAuthority(execution);
                user = authService.register(userDTO, RoleType.ADMIN);
            }
            else user = authService.register(userDTO, RoleType.CUSTOMER);

            execution.setVariable("userId", user.getUserId());
            execution.setVariable("email", user.getEmail());
            execution.setVariable("password", user.getPassword());
            execution.setVariable("role", user.getRole().name());

            TokenService.putUserToken(userId, token);
        }
        catch (Throwable throwable) {
            TokenService.putUserToken(userId, null);
            execution.setVariable("error", throwable.getMessage());
            throw new BpmnError("REGISTER_ERROR", throwable.getMessage());
        }
    }
}
