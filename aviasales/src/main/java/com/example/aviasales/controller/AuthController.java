package com.example.aviasales.controller;

import com.example.aviasales.dto.UserDTO;
import com.example.aviasales.entity.User;
import com.example.aviasales.service.AuthService;
import com.example.aviasales.util.enums.RoleType;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Tag(name = "Контроллер авторизации", description = "Авторизация")
public class AuthController {
    private AuthService authService;
    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> register(
            @Valid @RequestBody UserDTO userDTO
            ) {
        return ResponseEntity.ok(authService.register(userDTO, RoleType.CUSTOMER));
    }

    @Hidden
    @PostMapping(path = "/private/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> registerAdmin(
            @Valid @RequestBody UserDTO userDTO
    ) {
        return ResponseEntity.ok(authService.register(userDTO, RoleType.ADMIN));
    }
}
