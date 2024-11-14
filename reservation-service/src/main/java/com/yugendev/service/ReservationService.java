package com.yugendev.service;

import com.yugendev.model.Reservation;
import com.yugendev.repository.ReservationRepository;

import io.micronaut.context.annotation.Factory;
import jakarta.inject.Inject;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Factory
public class ReservationService {

    private final ReservationRepository reservationRepository;

    @Inject
    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }


    public Mono<Reservation> getReservationById(UUID reservationId) {
        return reservationRepository.findById(reservationId);
    }

    public Flux<Reservation> getReservationsByUserId(UUID userId) {
        return reservationRepository.findByUserId(userId);
    }

    public Flux<Reservation> getReservationsByShowtimeId(UUID showtimeId) {
        return reservationRepository.findByShowtimeId(showtimeId);
    }

    public Flux<Reservation> getReservationsByUserIdAndShowtimeId(UUID userId, UUID showtimeId) {
        return reservationRepository.findByUserIdAndShowtimeId(userId, showtimeId);
    }

    public Mono<Reservation> createReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

}
