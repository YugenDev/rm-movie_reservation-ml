package com.yugendev.showtime_service.controller;

import com.yugendev.showtime_service.model.Seat;
import com.yugendev.showtime_service.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
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

    @GetMapping("/{showtimeId}")
    public Flux<Seat> getSeatsByShowtimeId(@PathVariable UUID showtimeId) {
        return seatService.getSeatsByShowtimeId(showtimeId);
    }

    @GetMapping("/{showtimeId}/{seatNumber}")
    public Mono<Seat> getSeatByShowtimeIdAndSeatNumber(@PathVariable UUID showtimeId, @PathVariable String seatNumber) {
        return seatService.getSeatByShowtimeIdAndSeatNumber(showtimeId, seatNumber);
    }

    @PostMapping("/{showtimeId}/{seatNumber}/reserve")
    public Mono<Seat> reserveSeat(@PathVariable UUID showtimeId, @PathVariable String seatNumber) {
        return seatService.reserveSeat(showtimeId, seatNumber);
    }

    @DeleteMapping("/{showtimeId}/{seatNumber}")
    public Mono<Void> deleteSeat(@PathVariable UUID showtimeId, @PathVariable String seatNumber) {
        return seatService.deleteSeat(showtimeId, seatNumber);
    }

    @PutMapping("/{showtimeId}/{seatNumber}/unreserve")
    public Mono<Seat> unreserveSeat(@PathVariable UUID showtimeId, @PathVariable String seatNumber) {
        return seatService.unreserveSeat(showtimeId, seatNumber);
    }

}
