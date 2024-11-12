package com.yugendev.showtime_service.controller;

import com.yugendev.showtime_service.model.Seat;
import com.yugendev.showtime_service.service.SeatService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/seats")
public class SeatController {

    private final SeatService seatService;

    @Autowired
    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Seat> createSeat(@Valid @RequestBody Seat seat) {
        return seatService.createSeat(seat);
    }

    @GetMapping("/showtime/{showtimeId}/seat/{seatNumber}")
    public Mono<Seat> getSeatByShowtimeIdAndSeatNumber(@PathVariable UUID showtimeId, @PathVariable int seatNumber) {
        return seatService.getSeatByShowtimeIdAndSeatNumber(showtimeId, seatNumber);
    }

}
