package com.example.aviasales.util;

import com.example.aviasales.exception.MailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
public class EmailService {

    private JavaMailSender emailSender;

    @Value("${spring.mail.username}") private String sender;

    @Autowired
    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendHTMLMessage(String to, String subject, String text) throws MailException {

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setSubject(subject);
            helper.setFrom(sender);
            helper.setTo(to);

            boolean html = true;
            helper.setText(text, html);
        }
        catch (MessagingException e) {
            throw new MailException(to);
        }


        emailSender.send(message);
    }
}