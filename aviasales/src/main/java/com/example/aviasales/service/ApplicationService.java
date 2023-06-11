package com.example.aviasales.service;

import bitronix.tm.BitronixTransactionManager;
import com.example.aviasales.dto.ApplicationResponse;
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
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ApplicationService {
    Logger log = LoggerFactory.getLogger(ApplicationService.class);
    private ApplicationRepository applicationRepository;
    private UserRepository userRepository;
    private BitronixTransactionManager bitronixTransactionManager;
    private ObjectMapper objectMapper;
    private SimpMessagingTemplate template;

    @Autowired
    public ApplicationService(
            ApplicationRepository applicationRepository,
            BitronixTransactionManager bitronixTransactionManager,
            UserRepository userRepository,
            SimpMessagingTemplate template) {
        this.applicationRepository = applicationRepository;
        this.bitronixTransactionManager = bitronixTransactionManager;
        this.userRepository = userRepository;
        this.objectMapper = new ObjectMapper();
        this.template = template;
    }

    public Application addApplicationDeletePassenger(Long passengerId, Principal principal) {
        Application newApplication;
        try {
            bitronixTransactionManager.begin();

            String email = principal.getName();
            User user = userRepository.findByEmail(email);
            if (user == null) {
                throw new UserNotFoundException(email);
            }

            //TODO(проверяем, что заявка валидна)

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

            template.convertAndSend(
                    "/topic/applications-general",
                    new ApplicationResponse(
                            newApplication.getApplicationId(),
                            "Заявка на удаление пассажира была отправлена на обработку"
                    )
            );
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

    public Application addApplicationUpdatePassenger(Long passengerId, PassengerDTO passengerDTO, Principal principal) {
        Application newApplication;
        try {
            bitronixTransactionManager.begin();

            String email = principal.getName();
            User user = userRepository.findByEmail(email);
            if (user == null) {
                throw new UserNotFoundException(email);
            }
            //TODO(проверяем, что заявка валидна)

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

            template.convertAndSend(
                    "/topic/applications-general",
                    new ApplicationResponse(
                            newApplication.getApplicationId(),
                            "Заявка на изменение пассажира была отправлена на обработку"
                    )
            );
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

    //TODO(метод)
    public Long changeApplicationStatus(SetApplicationStatusRequestDTO setApplicationStatusRequestDTO) {
        return 1L;
    }

    public Set<Application> searchByStatus(ApplicationStatus applicationStatus) {
        return applicationRepository.findAll().stream().filter(it -> it.getApplicationStatus() == applicationStatus).collect(Collectors.toSet());
    }

    public Application getApplicationById(Long applicationId) {
        return applicationRepository.findById(applicationId).orElseThrow(() -> new ApplicationNotFoundException(applicationId));
    }

    public Long deleteApplication(Long applicationId) {
        try {
            bitronixTransactionManager.begin();

            getApplicationById(applicationId);
            applicationRepository.deleteById(applicationId);

            bitronixTransactionManager.commit();
            //TODO(отправить сообщение об изменении статуса заявки)
            return applicationId;
        }
        catch (Exception e) {
            try {
                bitronixTransactionManager.rollback();
            } catch (Exception ignore) {
                log.error("Unable to rollback transaction", ignore);
            }
            throw new TransactionException("deleting application - " + e.getMessage());
        }

    }
}
