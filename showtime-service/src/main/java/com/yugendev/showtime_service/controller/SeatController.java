package com.yugendev.showtime_service.controller;

import com.yugendev.showtime_service.model.Seat;
import com.yugendev.showtime_service.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/seats")
public class SeatController {

    private final SeatService seatService;

    @Autowired
    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    @GetMapping("/showtime/{showtimeId}")
    public Flux<Seat> getSeatsByShowtimeId(@PathVariable UUID showtimeId) {
        return seatService.getSeatsByShowtimeId(showtimeId);
    }

    @GetMapping("/showtime/{showtimeId}/seat/{seatNumber}")
    public Mono<Seat> getSeatByShowtimeIdAndSeatNumber(@PathVariable UUID showtimeId, @PathVariable String seatNumber) {
        return seatService.getSeatByShowtimeIdAndSeatNumber(showtimeId, seatNumber);
    }

    @PostMapping("/showtime/{showtimeId}/seat/{seatNumber}/reserve")
    public Mono<List<Seat>> reserveSeat(@PathVariable UUID showtimeId, @PathVariable List<String> seatNumber) {
        return seatService.reserveSeats(showtimeId, seatNumber);
    }

    @DeleteMapping("/showtime/{showtimeId}/seat/{seatNumber}")
    public Mono<Void> deleteSeat(@PathVariable UUID showtimeId, @PathVariable String seatNumber) {
        return seatService.deleteSeat(showtimeId, seatNumber);
    }

    @PutMapping("/showtime/{showtimeId}/seat/{seatNumber}/unreserve")
    public Mono<Seat> unreserveSeat(@PathVariable UUID showtimeId, @PathVariable String seatNumber) {
        return seatService.unreservedSeat(showtimeId, seatNumber);
    }

}
