package com.yugendev.controller;

import com.yugendev.model.Reservation;
import com.yugendev.service.ReservationService;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import jakarta.inject.Inject;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Controller("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    } 

    @Get("/user/{userId}")
    public Flux<Reservation> findByUserId(UUID userId) {
        return reservationService.findByUserId(userId);
    }

}
