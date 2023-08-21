package com.example.aviasales.service;

import com.example.aviasales.dto.UserDTO;
import com.example.aviasales.entity.User;
import com.example.aviasales.exception.UserAlreadyExistsException;
import com.example.aviasales.exception.not_found.UserNotFoundException;
import com.example.aviasales.repo.UserRepository;
import com.example.aviasales.util.enums.RoleType;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.AuthorizationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class AuthService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(UserDTO userDTO, RoleType role) {
        if (userRepository.findByEmail(userDTO.getEmail()) != null){
            throw new UserAlreadyExistsException(userDTO.getEmail());
        }

        log.error("register-password:" + userDTO.getPassword());
        log.error("encoded:" + passwordEncoder.encode(userDTO.getPassword()));
        User user = new User(
                null,
                userDTO.getEmail(),
                passwordEncoder.encode(userDTO.getPassword()),
                role,
                Set.of()
        );

        return userRepository.save(user);
    }

    public User auth(UserDTO userDTO) {
        User user = userRepository.findByEmail(userDTO.getEmail());
        if (user == null){
            throw new UserNotFoundException(userDTO.getEmail());
        }
        log.error("input-password:" + userDTO.getPassword());
        log.error("encoded:" + passwordEncoder.encode(userDTO.getPassword()));
        log.error("correct-password:" + user.getPassword());
        if (!passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
            throw new AuthorizationException("Incorrect password");
        }
        return user;
    }
}
