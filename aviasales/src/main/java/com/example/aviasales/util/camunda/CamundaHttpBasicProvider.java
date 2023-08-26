package com.example.aviasales.util.camunda;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
@Slf4j
public class CamundaHttpBasicProvider {
    private UserDetailsService userDetailsService;
    private Base64.Decoder decoder = Base64.getDecoder();
    private Base64.Encoder encoder = Base64.getEncoder();

    @Autowired
    public CamundaHttpBasicProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public String getEmailFromToken(String token) throws BadCredentialsException {
        byte[] decodedValue = decoder.decode(token);
        String encodedToken = new String(decodedValue);
        log.info("encoded-token = " + encodedToken);
        int delim = encodedToken.indexOf(":");
        if (delim == -1) {
            throw new BadCredentialsException("Invalid basic authentication token");
        }
        return encodedToken.substring(0, delim);
    }

    public Authentication getAuthentication(String token) {
        String email = getEmailFromToken(token);
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String encodeUsernamePassword(String username, String password) throws BadCredentialsException {
        String base64Token = username + ":" + password;
        try {
            return encoder.encodeToString(base64Token.getBytes());
        }
        catch (IllegalArgumentException ex) {
            throw new BadCredentialsException("Failed to decode basic authentication token");
        }
    }

}
