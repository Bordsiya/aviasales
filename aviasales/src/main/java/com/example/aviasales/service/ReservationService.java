package com.example.aviasales.service;

import com.example.aviasales.dto.ReservationDTO;
import com.example.aviasales.entity.Reservation;
import com.example.aviasales.exception.not_found.ReservationNotFoundException;
import com.example.aviasales.repo.ReservationRepository;
import com.example.aviasales.util.mappers.ReservationMapper;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {
    private ReservationRepository reservationRepository;
    private ReservationMapper reservationMapper;

    public ReservationService(
            ReservationRepository reservationRepository,
            ReservationMapper reservationMapper
    ) {
        this.reservationRepository = reservationRepository;
        this.reservationMapper = reservationMapper;
    }

    public Reservation getReservationById(Long reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException(reservationId));
    }

    public Reservation addReservation(ReservationDTO reservationDTO) {
        Reservation reservation = reservationMapper.fromDto(reservationDTO);
        return reservationRepository.save(reservation);
    }
}
