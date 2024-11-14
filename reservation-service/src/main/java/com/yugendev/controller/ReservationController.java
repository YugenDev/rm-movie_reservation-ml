package com.yugendev.controller;

import com.yugendev.model.Reservation;
import com.yugendev.service.ReservationService;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import jakarta.inject.Inject;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Controller("/reservations")
public class ReservationController {

    @Inject
    private ReservationService reservationService;


    @Get("/{reservationId}")
    public Mono<Reservation> getReservationById(UUID reservationId) {
        return reservationService.getReservationById(reservationId);
    }

    @Get("/user/{userId}")
    public Flux<Reservation> getReservationsByUserId(UUID userId) {
        return reservationService.getReservationsByUserId(userId);
    }

    @Get("/showtime/{showtimeId}")
    public Flux<Reservation> getReservationsByShowtimeId(UUID showtimeId) {
        return reservationService.getReservationsByShowtimeId(showtimeId);
    }

    @Get("/user/{userId}/showtime/{showtimeId}")
    public Flux<Reservation> getReservationsByUserIdAndShowtimeId(UUID userId, UUID showtimeId) {
        return reservationService.getReservationsByUserIdAndShowtimeId(userId, showtimeId);
    }

    @Post("/")
    public Mono<Reservation> createReservation(Reservation reservation) {
        return reservationService.createReservation(reservation);
    }

}
