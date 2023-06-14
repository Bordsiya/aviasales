package com.example.aviasales.service;

import bitronix.tm.BitronixTransactionManager;
import com.example.aviasales.dto.PassengerDTO;
import com.example.aviasales.dto.requests.SetApplicationStatusRequestDTO;
import com.example.aviasales.entity.Application;
import com.example.aviasales.entity.User;
import com.example.aviasales.exception.TransactionException;
import com.example.aviasales.exception.not_found.ApplicationNotFoundException;
import com.example.aviasales.exception.not_found.UserNotFoundException;
import com.example.aviasales.repo.ApplicationRepository;
import com.example.aviasales.repo.UserRepository;
import com.example.aviasales.util.enums.ApplicationStatus;
import com.example.aviasales.util.enums.ApplicationType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Service
public class ApplicationService {
    Logger log = LoggerFactory.getLogger(ApplicationService.class);
    private ApplicationRepository applicationRepository;
    private UserRepository userRepository;
    private BitronixTransactionManager bitronixTransactionManager;
    private ObjectMapper objectMapper;

    @Autowired
    public ApplicationService(
            ApplicationRepository applicationRepository,
            BitronixTransactionManager bitronixTransactionManager,
            UserRepository userRepository) {
        this.applicationRepository = applicationRepository;
        this.bitronixTransactionManager = bitronixTransactionManager;
        this.userRepository = userRepository;
        this.objectMapper = new ObjectMapper();
    }

    public Application addApplicationDeletePassenger(Long passengerId, String email) {
        Application newApplication;
        try {
            bitronixTransactionManager.begin();

            User user = userRepository.findByEmail(email);
            if (user == null) {
                throw new UserNotFoundException(email);
            }

            Application application = new Application(
                    null,
                    user,
                    ApplicationType.DELETE_PASSENGER_APPLICATION,
                    objectMapper.writeValueAsString(passengerId),
                    ApplicationStatus.ON_MODERATION,
                    LocalDate.now(),
                    Boolean.FALSE
            );

            newApplication = applicationRepository.save(application);

            bitronixTransactionManager.commit();

            // TODO(кинуть в тредпул для хэндлеров заявок)
            return newApplication;
        }
        catch (Exception e) {
            try {
                bitronixTransactionManager.rollback();
            } catch (Exception ignore) {
                log.error("Unable to rollback transaction", ignore);
            }
            throw new TransactionException("adding application - " + e.getMessage());
        }
    }

    public Application addApplicationUpdatePassenger(Long passengerId, PassengerDTO passengerDTO, String email) {
        Application newApplication;
        try {
            bitronixTransactionManager.begin();

            User user = userRepository.findByEmail(email);
            if (user == null) {
                throw new UserNotFoundException(email);
            }

            Application application = new Application(
                    null,
                    user,
                    ApplicationType.UPDATE_PASSENGER_APPLICATION,
                    objectMapper.writeValueAsString(passengerId)
                            + objectMapper.writeValueAsString(passengerDTO),
                    ApplicationStatus.ON_MODERATION,
                    LocalDate.now(),
                    Boolean.FALSE
            );

            newApplication = applicationRepository.save(application);

            bitronixTransactionManager.commit();

            // TODO(кинуть в тредпул для хэндлеров заявок)
            return newApplication;
        }
        catch (Exception e) {
            try {
                bitronixTransactionManager.rollback();
            } catch (Exception ignore) {
                log.error("Unable to rollback transaction", ignore);
            }
            throw new TransactionException("adding application - " + e.getMessage());
        }
    }

    public Application changeApplicationStatus(SetApplicationStatusRequestDTO setApplicationStatusRequestDTO) {
        Application application = getApplicationById(setApplicationStatusRequestDTO.getApplicationId());
        application.setApplicationStatus(ApplicationStatus.valueOf(setApplicationStatusRequestDTO.getApplicationStatus()));
        return applicationRepository.save(application);
    }

    public Application getApplicationById(Long applicationId) {
        return applicationRepository.findById(applicationId).orElseThrow(() -> new ApplicationNotFoundException(applicationId));
    }

    public Set<Application> getUserApplications(String email) {
        Set<Application> applications = new HashSet<>();
        try {
            bitronixTransactionManager.begin();

            User user = userRepository.findByEmail(email);
            if (user == null) {
                throw new UserNotFoundException(email);
            }

            applications.addAll(applicationRepository.findAllByUser(user));

            bitronixTransactionManager.commit();

            return applications;
        }
        catch (Exception e) {
            try {
                bitronixTransactionManager.rollback();
            } catch (Exception ignore) {
                log.error("Unable to rollback transaction", ignore);
            }
            throw new TransactionException("adding application - " + e.getMessage());
        }
    }
}
