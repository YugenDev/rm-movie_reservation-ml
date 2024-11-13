package com.yugendev.showtime_service.controller;

import com.yugendev.showtime_service.controller.dto.SeatReservationRequest;
import com.yugendev.showtime_service.model.Seat;
import com.yugendev.showtime_service.model.Showtime;
import com.yugendev.showtime_service.service.ShowtimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/showtimes")
public class ShowtimeController {

    private static final Logger logger = LoggerFactory.getLogger(ShowtimeController.class);

    private final ShowtimeService showtimeService;

    @Autowired
    public ShowtimeController(ShowtimeService showtimeService) {
        this.showtimeService = showtimeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Showtime> createShowtime(@RequestBody Showtime showtime) {
        return showtimeService.createShowtime(showtime);
    }

    @GetMapping
    public Flux<Showtime> getAllShowtimes() {
        return showtimeService.getAllShowtimes();
    }

    @GetMapping("/{id}/seats/avalible")
    public Flux<Seat> getAvalibleSeats(@PathVariable UUID id) {
        return showtimeService.getAvalibleSeats(id);
    }

    @GetMapping("/{id}/seats")
    public Flux<Seat> getAllSeats(@PathVariable UUID id) {
        return showtimeService.getAllSeats(id);
    }

    @PostMapping("/{id}/seats/reserve")
    public Mono<List<Seat>> reserveSeat(@PathVariable UUID id, @RequestBody SeatReservationRequest seatNumbers) {
        logger.debug("Received request to reserve seats for showtimeId: {} with seatNumbers: {}", id, seatNumbers);
        return showtimeService.reserveSeats(id, seatNumbers.getSeatNumbers());
    }

    @GetMapping("/{id}")
    public Mono<Showtime> findShowtimeById(@PathVariable UUID id) {
        return showtimeService.getShowtimeById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteShowtimeById(@PathVariable UUID id) {
        return showtimeService.deleteShowtimeById(id);
    }
}
