package com.yugendev.showtime_service.controller;

import com.yugendev.showtime_service.model.Showtime;
import com.yugendev.showtime_service.service.ShowtimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/showtimes")
public class ShowtimeController {

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

    @GetMapping("/{id}")
    public Mono<Showtime> getShowtimeById(@PathVariable UUID id) {
        return showtimeService.getShowtimeById(id);
    }
}
