package com.example.aviasales.controller;

import com.example.aviasales.dto.AckDto;
import com.example.aviasales.exception.not_found.MailRequestNotFoundException;
import com.example.aviasales.repo.MailRequestRepository;
import com.example.aviasales.util.enums.MailRequestStatus;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/mail")
@RequiredArgsConstructor
@Tag(name = "Контроллер подтверждения емэйлов", description = "Описание возможностей подтверждения")
@SecurityRequirement(name = "basicAuth")
public class MailAckController {
    private final MailRequestRepository repository;

    @PostMapping(name = "/ack", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> addPassengers(@RequestBody AckDto ackDto) {
        var found = repository.findById(ackDto.id());
        found.orElseThrow(() -> new MailRequestNotFoundException(ackDto.id()));
        var maiLRequest = found.get();
        maiLRequest.setStatus(MailRequestStatus.SENT);
        repository.save(maiLRequest);
        return ResponseEntity.ok(ackDto.id());
    }
}
