package com.example.aviasales.util.mappers;

import com.example.aviasales.dto.PassengerDTO;
import com.example.aviasales.entity.Flight;
import com.example.aviasales.entity.Passenger;
import com.example.aviasales.entity.Reservation;
import com.example.aviasales.entity.Tariff;
import com.example.aviasales.util.enums.DocumentType;
import com.example.aviasales.util.enums.Gender;
import org.springframework.stereotype.Component;

@Component
public class PassengerMapper {
    public Passenger fromDto(PassengerDTO passengerDTO,
                             Tariff tariff,
                             Flight flight,
                             Reservation reservation
                             ) {
        return new Passenger(
                null,
                passengerDTO.getFirstName(),
                passengerDTO.getLastName(),
                passengerDTO.getPatronymic(),
                Gender.valueOf(passengerDTO.getGender()),
                passengerDTO.getCitizenship(),
                passengerDTO.getIsKid(),
                DocumentType.valueOf(passengerDTO.getDocumentType()),
                passengerDTO.getDocumentNumber(),
                passengerDTO.getExpirationDate(),
                passengerDTO.getHasHearingDifficulties(),
                passengerDTO.getHasVisionDifficulties(),
                passengerDTO.getRequiredWheelchair(),
                flight,
                tariff,
                reservation
        );
    }
}
