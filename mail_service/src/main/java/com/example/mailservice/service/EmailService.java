package com.example.mailservice.service;

import com.example.mailservice.exception.MailException;
import com.example.mailservice.model.EmailRequest;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    public void send(EmailRequest request) throws MailException {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setFrom(senderEmail);
            helper.setSubject(request.getSubject());
            helper.setTo(request.getEmail());
            helper.setText(request.getText(), true);

            emailSender.send(message);
        } catch (Exception e) {
            throw new MailException(request.getEmail());
        }
    }
}
