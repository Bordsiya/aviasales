package com.example.aviasales.util.mappers;

import com.example.aviasales.dto.ReservationDTO;
import com.example.aviasales.entity.Reservation;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ReservationMapper {
    public Reservation fromDto(ReservationDTO reservationDTO) {
        return new Reservation(
                null,
                reservationDTO.getReservationCode(),
                LocalDateTime.now(),
                reservationDTO.getPhoneNumber(),
                reservationDTO.getEmail(),
                null
        );
    }
}
