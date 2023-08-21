package com.example.aviasales.util.camunda;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CamundaHttpBasicFilter {
    private CamundaHttpBasicProvider camundaHttpBasicProvider;
    @Autowired
    public CamundaHttpBasicFilter(CamundaHttpBasicProvider camundaHttpBasicProvider) {
        this.camundaHttpBasicProvider = camundaHttpBasicProvider;
    }
    public void doFilter(String token) {
        if (token != null) {
            Authentication auth = camundaHttpBasicProvider.getAuthentication(token);

            if (auth != null) {
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
    }
}
