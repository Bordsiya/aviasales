package com.example.aviasales.service;

import com.example.aviasales.dto.AddPassengersDTO;
import com.example.aviasales.dto.PassengerDTO;
import com.example.aviasales.dto.ReservationDTO;
import com.example.aviasales.entity.*;
import com.example.aviasales.exception.MailException;
import com.example.aviasales.exception.NoAdultsForFlightException;
import com.example.aviasales.exception.not_match.AirlinesNotMatchesException;
import com.example.aviasales.exception.NotEnoughSeatsInAircraftException;
import com.example.aviasales.exception.not_match.DocumentTypeNotMachKidException;
import com.example.aviasales.repo.PassengerRepository;
import com.example.aviasales.util.EmailService;
import com.example.aviasales.util.enums.DocumentType;
import com.example.aviasales.util.Utils;
import com.example.aviasales.util.mappers.PassengerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

@Service
public class PassengerService {
    Logger logger = LoggerFactory.getLogger(PassengerService.class);
    private PassengerRepository passengerRepository;
    private TariffService tariffService;
    private FlightService flightService;
    private ReservationService reservationService;
    private PassengerMapper passengerMapper;
    private EmailService emailService;
    @Autowired
    public PassengerService(
            PassengerRepository passengerRepository,
            TariffService tariffService,
            FlightService flightService,
            ReservationService reservationService,
            PassengerMapper passengerMapper,
            EmailService emailService
    ) {
        this.passengerRepository = passengerRepository;
        this.tariffService = tariffService;
        this.flightService = flightService;
        this.reservationService = reservationService;
        this.passengerMapper = passengerMapper;
        this.emailService = emailService;
    }

    @Transactional(rollbackOn = Throwable.class)
    public Set<Passenger> addPassengers(AddPassengersDTO addPassengersDTO) {
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

        for (PassengerDTO passengerDTO: addPassengersDTO.getPassengers()) {
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
        for (PassengerDTO passengerDTO: addPassengersDTO.getPassengers()) {
            Tariff tariff = tariffService.getTariffById(passengerDTO.getTariffId());
            Passenger newPassenger = passengerMapper.fromDto(passengerDTO, tariff, flight, reservation);

            passengers.add(passengerRepository.save(newPassenger));
        }

        sendAddPassengersEmail(flight, reservation, passengers);
        return passengers;
    }

    private void sendAddPassengersEmail(Flight flight, Reservation reservation, Set<Passenger> passengers) {
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
        for (Passenger passenger: passengers) {
            passengersText.append(passenger.getFirstName()).append(" ").append(passenger.getLastName()).append(" ").append(passenger.getPatronymic()).append("</br>");
            passengersText.append("Документ: ").append(passenger.getDocumentType().documentName).append("</br>");
            passengersText.append("Номер документа: ").append(passenger.getDocumentNumber()).append("</br>");
            passengersText.append("</br>");
        }

        try {
            emailService.sendHTMLMessage(
                    reservation.getEmail(),
                    flight.getAircraft().getAirline().getAirlineName(),
                    String.format(textBase, passengersText)
            );
        } catch (MessagingException e) {
            throw new MailException(reservation.getEmail());
        }
    }
}
