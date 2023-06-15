package com.example.aviasales.service;

import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import bitronix.tm.BitronixTransactionManager;
import com.example.aviasales.controller.StompController;
import com.example.aviasales.dto.PassengerDTO;
import com.example.aviasales.dto.ReservationDTO;
import com.example.aviasales.dto.requests.AddPassengersDTO;
import com.example.aviasales.dto.requests.DeletePassengersDTO;
import com.example.aviasales.dto.requests.MailServiceRequest;
import com.example.aviasales.entity.Aircraft;
import com.example.aviasales.entity.Flight;
import com.example.aviasales.entity.MailRequest;
import com.example.aviasales.entity.Passenger;
import com.example.aviasales.entity.Reservation;
import com.example.aviasales.entity.Tariff;
import com.example.aviasales.exception.NoAdultsForFlightException;
import com.example.aviasales.exception.NotEnoughSeatsInAircraftException;
import com.example.aviasales.exception.TransactionException;
import com.example.aviasales.exception.not_found.PassengerNotFoundException;
import com.example.aviasales.exception.not_match.AirlinesNotMatchesException;
import com.example.aviasales.exception.not_match.DocumentTypeNotMachKidException;
import com.example.aviasales.repo.MailRequestRepository;
import com.example.aviasales.repo.PassengerRepository;
import com.example.aviasales.repo.UserRepository;
import com.example.aviasales.service.rabbitmq.MailRequestPublisher;
import com.example.aviasales.util.Utils;
import com.example.aviasales.util.enums.DocumentType;
import com.example.aviasales.util.enums.Gender;
import com.example.aviasales.util.enums.MailRequestStatus;
import com.example.aviasales.util.mappers.PassengerMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class PassengerService {
    Logger log = LoggerFactory.getLogger(PassengerService.class);
    private PassengerRepository passengerRepository;
    private TariffService tariffService;
    private FlightService flightService;
    private ReservationService reservationService;
    private PassengerMapper passengerMapper;
    private BitronixTransactionManager bitronixTransactionManager;

    private UserRepository userRepository;
    private final MailRequestRepository mailRequestRepository;

    private ObjectMapper mapper;

    private final StompController stompController;

    @Autowired
    public PassengerService(
            PassengerRepository passengerRepository,
            @Lazy TariffService tariffService,
            @Lazy FlightService flightService,
            @Lazy ReservationService reservationService,
            PassengerMapper passengerMapper,
            BitronixTransactionManager bitronixTransactionManager,
            MailRequestRepository mailRequestRepository,
            UserRepository userRepository,
            ObjectMapper mapper,
            StompController stompController
    ) {
        this.passengerRepository = passengerRepository;
        this.tariffService = tariffService;
        this.flightService = flightService;
        this.reservationService = reservationService;
        this.passengerMapper = passengerMapper;
        this.bitronixTransactionManager = bitronixTransactionManager;
        this.mailRequestRepository = mailRequestRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.stompController = stompController;
    }

    public Passenger getPassengerById(Long passengerId) {
        return passengerRepository.findById(passengerId).orElseThrow(() -> new PassengerNotFoundException(passengerId));
    }

    public Set<Passenger> addPassengers(AddPassengersDTO addPassengersDTO) {
        try {
            bitronixTransactionManager.begin();
            Flight flight = flightService.getFlightById(addPassengersDTO.getFlightId());

            Aircraft aircraft = flight.getAircraft();
            Long amountOfRegisteredPassengers = passengerRepository.countByFlightId(flight.getFlightId());
            if (amountOfRegisteredPassengers + addPassengersDTO.getPassengers().size() > aircraft.getNumberOfSeats()) {
                throw new NotEnoughSeatsInAircraftException(
                        aircraft.getAircraftId(), aircraft.getNumberOfSeats()
                );
            }

            long amountOfAdults = addPassengersDTO.getPassengers().stream()
                    .filter(passengerDTO -> passengerDTO.getIsKid() == Boolean.FALSE)
                    .count();
            if (amountOfAdults == 0) {
                throw new NoAdultsForFlightException(flight.getFlightId());
            }

            for (PassengerDTO passengerDTO : addPassengersDTO.getPassengers()) {
                Tariff tariff = tariffService.getTariffById(passengerDTO.getTariffId());
                if (!tariff.getAirline().equals(flight.getAircraft().getAirline())) {
                    throw new AirlinesNotMatchesException(
                            "Flight", "Tariff",
                            flight.getAircraft().getAirline().getAirlineId(),
                            tariff.getAirline().getAirlineId()
                    );
                }

                if (passengerDTO.getIsKid() == Boolean.TRUE && (passengerDTO.getDocumentType().equalsIgnoreCase(DocumentType.MILITARY_RECORD.name())
                        || passengerDTO.getDocumentType().equalsIgnoreCase(DocumentType.PASSPORT.name()))
                ) {
                    throw new DocumentTypeNotMachKidException(passengerDTO.getDocumentType(), passengerDTO.getIsKid());
                }
            }

            Reservation reservation = reservationService.addReservation(new ReservationDTO(
                    Utils.generateReservationCode(),
                    addPassengersDTO.getPhoneNumber(),
                    addPassengersDTO.getEmail()
            ));

            Set<Passenger> passengers = new HashSet<>();
            for (PassengerDTO passengerDTO : addPassengersDTO.getPassengers()) {
                Tariff tariff = tariffService.getTariffById(passengerDTO.getTariffId());
                Passenger newPassenger = passengerMapper.fromDto(passengerDTO, tariff, flight, reservation);

                passengers.add(passengerRepository.save(newPassenger));
            }

            var user = userRepository.findByEmail(addPassengersDTO.getEmail());
            var mailRequestEntity = mailRequestRepository.save(
                    MailRequest.builder()
                            .createdAt(Instant.now())
                            .status(MailRequestStatus.SENT)
                            .payload("")
                            .user(user)
                            .build()
            );

            var mailRequest = MailServiceRequest.builder()
                    .mailRequestId(mailRequestEntity.getId())
                    .email(addPassengersDTO.getEmail())
                    .subject(flight.getAircraft().getAirline().getAirlineName())
                    .text(buildAddPassengersText(flight, reservation, passengers))
                    .build();
            mailRequestEntity.setPayload(mapper.writeValueAsString(mailRequest));
            mailRequestRepository.save(mailRequestEntity);
            stompController.send(
                    "process-mail-message",
                    mailRequest
            );
            bitronixTransactionManager.commit();
            return passengers;
        } catch (Exception e) {
            log.error("Error", e);
            try {
                bitronixTransactionManager.rollback();
            } catch (Exception ignore) {
                log.error("Unable to rollback transaction", ignore);
            }
            throw new TransactionException("adding passengers - " + e.getMessage());
        }
    }

    public Set<Long> deletePassengers(DeletePassengersDTO deletePassengersDTO) {
        try {
            bitronixTransactionManager.begin();
            Set<Long> deletePassengersIds = new HashSet<>(deletePassengersDTO.getPassengersIds());
            Map<String, String> reservationCodeToEmail = new HashMap<>();
            Map<String, String> reservationCodeToAirlineName = new HashMap<>();
            for (Long passengerId : deletePassengersIds) {
                deletePassengersIds.add(passengerId);

                Passenger passenger = getPassengerById(passengerId);
                Set<Long> passengersWithTheSameReservation = passengerRepository
                        .getPassengersBySameReservation(passenger.getReservation().getReservationId());
                if (passengersWithTheSameReservation.size() == 1) {
                    reservationService.deleteReservation(passenger.getReservation().getReservationId());
                } else {
                    long amountOfAdultsAfterDeleting = 0;
                    for (Long passengerReservationId : passengersWithTheSameReservation) {
                        if (getPassengerById(passengerReservationId).getIsKid() == Boolean.FALSE) {
                            amountOfAdultsAfterDeleting++;
                        }
                    }
                    if (passenger.getIsKid() == Boolean.FALSE) {
                        amountOfAdultsAfterDeleting--;
                    }
                    if (amountOfAdultsAfterDeleting == 0) {
                        throw new NoAdultsForFlightException(passenger.getFlight().getFlightId());
                    }
                    passengerRepository.deleteById(passengerId);
                }
                if (!reservationCodeToEmail.containsKey(passenger.getReservation().getReservationCode())) {
                    reservationCodeToEmail.put(
                            passenger.getReservation().getReservationCode(),
                            passenger.getReservation().getEmail());
                    reservationCodeToAirlineName.put(
                            passenger.getReservation().getReservationCode(),
                            passenger.getFlight().getAircraft().getAirline().getAirlineName()
                    );
                }
            }
            for (Map.Entry<String, String> entry : reservationCodeToEmail.entrySet()) {
                stompController.send(
                        "process-mail-message",
                        new MailServiceRequest(
                                1L, // TODO(добавить добавление в бд)
                                entry.getValue(),
                                reservationCodeToAirlineName.get(entry.getKey()),
                                buildDeletePassengersEmail(entry.getKey())
                        )
                );
            }
            bitronixTransactionManager.commit();
            return deletePassengersIds;
        } catch (Exception e) {
            try {
                bitronixTransactionManager.rollback();
            } catch (Exception ignore) {
                log.error("Unable to rollback transaction", ignore);
            }
            throw new TransactionException("deleting passengers - " + e.getMessage());
        }
    }

    public Passenger updatePassenger(Long passengerId, PassengerDTO passengerDTO) {
        Passenger passenger = getPassengerById(passengerId);
        passenger.setFirstName(passengerDTO.getFirstName());
        passenger.setLastName(passengerDTO.getLastName());
        passenger.setPatronymic(passengerDTO.getPatronymic());
        passenger.setGender(Gender.valueOf(passengerDTO.getGender()));
        passenger.setCitizenship(passengerDTO.getCitizenship());

        if (passenger.getIsKid() == Boolean.FALSE && passengerDTO.getIsKid() == Boolean.TRUE) {
            Set<Long> passengersWithTheSameReservation = passengerRepository
                    .getPassengersBySameReservation(passenger.getReservation().getReservationId());
            long amountOfAdults = 0;
            for (Long passengerReservationId : passengersWithTheSameReservation) {
                if (getPassengerById(passengerReservationId).getIsKid() == Boolean.FALSE) {
                    amountOfAdults++;
                }
            }
            if (amountOfAdults == 1) {
                throw new NoAdultsForFlightException(passenger.getFlight().getFlightId());
            }
        }

        passenger.setIsKid(passengerDTO.getIsKid());

        if (passenger.getIsKid() == Boolean.TRUE && (passengerDTO.getDocumentType().equalsIgnoreCase(DocumentType.MILITARY_RECORD.name())
                || passengerDTO.getDocumentType().equalsIgnoreCase(DocumentType.PASSPORT.name()))
        ) {
            throw new DocumentTypeNotMachKidException(passengerDTO.getDocumentType(), passengerDTO.getIsKid());
        }

        passenger.setDocumentType(DocumentType.valueOf(passengerDTO.getDocumentType()));
        passenger.setDocumentNumber(passengerDTO.getDocumentNumber());
        passenger.setExpirationDate(passengerDTO.getExpirationDate());
        passenger.setHasHearingDifficulties(passengerDTO.getHasHearingDifficulties());
        passenger.setHasVisionDifficulties(passengerDTO.getHasVisionDifficulties());
        passenger.setRequiredWheelchair(passengerDTO.getRequiredWheelchair());

        Tariff tariff = tariffService.getTariffById(passengerDTO.getTariffId());
        if (!tariff.getAirline().equals(passenger.getFlight().getAircraft().getAirline())) {
            throw new AirlinesNotMatchesException(
                    "Flight", "Tariff",
                    passenger.getFlight().getAircraft().getAirline().getAirlineId(),
                    tariff.getAirline().getAirlineId()
            );
        }
        passenger.setTariff(tariffService.getTariffById(passengerDTO.getTariffId()));

        Passenger newPassenger = passengerRepository.save(passenger);

        stompController.send(
                "process-mail-message",
                new MailServiceRequest(
                        1L, // TODO(добавить добавление в бд)
                        passenger.getReservation().getEmail(),
                        passenger.getFlight().getAircraft().getAirline().getAirlineName(),
                        buildUpdatePassenger(passenger.getReservation(), passenger)
                )
        );

        return newPassenger;
    }

    private String buildAddPassengersText(Flight flight, Reservation reservation, Set<Passenger> passengers) {
        String textBase = "<b>Ваши билеты на aviasales.ru по заказу " + reservation.getReservationCode() + "</b>,<br>" +
                "<i>Аэропорт вылета: " + flight.getDepartureAirport().getAirportName() + "</br>" +
                "Аэропорт приземления: " + flight.getArrivalAirport().getAirportName() + "</br>" +
                "Самолёт: " + flight.getAircraft().getModel() + "</br>" +
                "Дата вылета: " + flight.getDepartureDate().toString() + "</br>" +
                "Время вылета: " + flight.getDepartureTime().toString() + "</br>" +
                "Дата приземления: " + flight.getArrivalDate().toString() + "</br>" +
                "Время приземления: " + flight.getArrivalTime().toString() + "</br>" +
                "</br> </br>" +
                "Путешественики: </br>" +
                "%s" +
                "</i>";

        StringBuilder passengersText = new StringBuilder();
        for (Passenger passenger : passengers) {
            passengersText.append(passenger.getFirstName()).append(" ").append(passenger.getLastName()).append(" ").append(passenger.getPatronymic()).append("</br>");
            passengersText.append("Документ: ").append(passenger.getDocumentType().documentName).append("</br>");
            passengersText.append("Номер документа: ").append(passenger.getDocumentNumber()).append("</br>");
            passengersText.append("</br>");
        }
        return String.format(textBase, passengersText);
    }

    private String buildDeletePassengersEmail(String reservationCode) {
        return "<b>Ваши билеты на aviasales.ru по заказу " + reservationCode + " больше недействительны </b>,<br>" +
                "<i>Ваш отказ от билета был обработан </br></i>";
    }


    private String buildUpdatePassenger(Reservation reservation, Passenger passenger) {
        return "<b>Были изменены данные о пользователе в заказе " + reservation.getReservationCode() + "</b>,<br>" +
                "<i>Пользователь: </br>" +
                passenger.getFirstName() + " " + passenger.getLastName() + " " + passenger.getPatronymic() + "</br" +
                "></i>";
    }
}
