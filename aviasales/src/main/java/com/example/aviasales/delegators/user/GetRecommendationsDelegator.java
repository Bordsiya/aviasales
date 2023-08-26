package com.example.aviasales.delegators.user;

import com.example.aviasales.dto.RecommendationDto;
import com.example.aviasales.service.RecommendationService;
import com.example.aviasales.service.camunda.DelegateAuthCheckService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.inject.Named;
import java.util.List;

@Named
public class GetRecommendationsDelegator implements JavaDelegate {
    private RecommendationService recommendationService;
    private DelegateAuthCheckService delegateAuthCheckService;
    private ObjectMapper objectMapper;
    @Autowired
    public GetRecommendationsDelegator(RecommendationService recommendationService,
                                       DelegateAuthCheckService delegateAuthCheckService,
                                       ObjectMapper objectMapper) {
        this.recommendationService = recommendationService;
        this.delegateAuthCheckService = delegateAuthCheckService;
        this.objectMapper = objectMapper;
    }
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try {
            delegateAuthCheckService.checkCustomerAuthority(execution);
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            List<RecommendationDto> recommendations = recommendationService.getAllUserRecommendations(email);
            execution.setVariable("result", objectMapper.writeValueAsString(recommendations));
        }
        catch (Throwable throwable) {
            execution.setVariable("error", throwable.getMessage());
            throw new BpmnError("GET_RECOMMENDATIONS_ERROR", throwable.getMessage());
        }
    }
}
