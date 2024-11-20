package com.yugendev.service;

import com.yugendev.model.Reservation;
import com.yugendev.repository.ReservationRepository;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Singleton
public class ReservationService {

    @Inject
    private ReservationRepository reservationRepository;

    public Flux<Reservation> findByUserId(UUID userId) {
        return reservationRepository.findByUserId(userId);
    }

}
