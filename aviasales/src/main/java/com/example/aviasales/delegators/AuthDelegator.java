package com.example.aviasales.delegators;

import com.example.aviasales.dto.UserDTO;
import com.example.aviasales.entity.User;
import com.example.aviasales.service.AuthService;
import com.example.aviasales.service.camunda.TokenService;
import com.example.aviasales.util.camunda.CamundaHttpBasicProvider;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Named;

@Named
public class AuthDelegator implements JavaDelegate {
    private AuthService authService;
    private CamundaHttpBasicProvider camundaHttpBasicProvider;

    @Autowired
    public AuthDelegator(AuthService authService,
                         CamundaHttpBasicProvider camundaHttpBasicProvider) {
        this.authService = authService;
        this.camundaHttpBasicProvider = camundaHttpBasicProvider;
    }
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String userId = execution.getProcessEngineServices().getIdentityService().getCurrentAuthentication().getUserId();
        try {
            UserDTO userDTO = new UserDTO(
                    (String) execution.getVariable("email"),
                    (String) execution.getVariable("password")
            );

            User user = authService.auth(userDTO);

            execution.setVariable("userId", user.getUserId());
            execution.setVariable("email", user.getEmail());
            execution.setVariable("password", user.getPassword());
            execution.setVariable("role", user.getRole().name());

            String token = camundaHttpBasicProvider.encodeUsernamePassword(user.getEmail(), user.getPassword());
            TokenService.putUserToken(userId, token);
        } catch (Throwable throwable) {
            TokenService.putUserToken(userId, null);
            execution.setVariable("error", throwable.getMessage());
            throw new BpmnError("AUTH_ERROR", throwable.getMessage());
        }
    }
}
