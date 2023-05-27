package com.example.aviasales.service;

import com.example.aviasales.dto.UserDTO;
import com.example.aviasales.entity.User;
import com.example.aviasales.exception.UserAlreadyExistsException;
import com.example.aviasales.exception.not_found.UserNotFoundException;
import com.example.aviasales.repo.UserRepository;
import com.example.aviasales.util.enums.RoleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
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

        User user = new User(
                null,
                userDTO.getEmail(),
                passwordEncoder.encode(userDTO.getPassword()),
                role
        );

        return userRepository.save(user);
    }
}
