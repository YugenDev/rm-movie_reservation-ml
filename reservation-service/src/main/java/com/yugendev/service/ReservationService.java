package com.yugendev.service;

import com.yugendev.model.Reservation;
import com.yugendev.repository.ReservationRepository;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Singleton
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public Flux<Reservation> findByUserId(UUID userId) {
        return reservationRepository.findByUserId(userId);
    }

    public Mono<Reservation> findByReservationId(UUID reservationId) {
        return reservationRepository.findByReservationId(reservationId);
    }

}
