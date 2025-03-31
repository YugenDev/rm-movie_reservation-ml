package com.yugendev.showtime_service.controller;

import com.yugendev.showtime_service.controller.dto.SeatReservationRequest;
import com.yugendev.showtime_service.model.Seat;
import com.yugendev.showtime_service.model.Showtime;
import com.yugendev.showtime_service.service.SeatService;
import com.yugendev.showtime_service.service.ShowtimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/showtimes")
public class ShowtimeController {

    private final ShowtimeService showtimeService;
    private final SeatService seatService;

    @Autowired
    public ShowtimeController(ShowtimeService showtimeService, SeatService seatService) {
        this.showtimeService = showtimeService;
        this.seatService = seatService;
    }

    @GetMapping("/{showtimeId}/{seatNumber}")
    public Mono<Seat> getSeatByShowtimeIdAndSeatNumber(@PathVariable UUID showtimeId, @PathVariable String seatNumber) {
        return seatService.getSeatByShowtimeIdAndSeatNumber(showtimeId, seatNumber);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Showtime> createShowtime(@RequestBody Showtime showtime) {
        return showtimeService.createShowtime(showtime);
    }

    @PostMapping("/{id}/seats/reserve")
    public Mono<List<Seat>> reserveSeats(@PathVariable UUID id, @RequestBody SeatReservationRequest seatReservationRequest) {
        return seatService.reserveSeats(id, seatReservationRequest.getSeatNumbers());
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

    @GetMapping("/{id}")
    public Mono<Showtime> findShowtimeById(@PathVariable UUID id) {
        return showtimeService.getShowtimeById(id);
    }

    @PutMapping("/{id}")
    public Mono<Showtime> updateShowtime(@PathVariable UUID id,@RequestBody Showtime showtime) {
        return showtimeService.updateShowtime(id, showtime);
    }

    @PutMapping("/{id}/seats/unreserve")
    public Mono<Seat> unreserveSeat(@PathVariable UUID id, @RequestBody String seatNumber) {
        return seatService.unreservedSeat(id, seatNumber);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Void> deleteShowtimeById(@PathVariable UUID id) {
        return showtimeService.deleteShowtimeById(id);
    }

}
