package com.yugendev.showtime_service.service;

import com.yugendev.showtime_service.model.Showtime;
import com.yugendev.showtime_service.repository.ShowtimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class ShowtimeService {

    private final ShowtimeRepository showtimeRepository;

    @Autowired
    public ShowtimeService(ShowtimeRepository showtimeRepository) {
        this.showtimeRepository = showtimeRepository;
    }

    public Flux<Showtime> getAllShowtimes() {
        return showtimeRepository.findAll();
    }

    public Mono<Showtime> getShowtimeById(UUID id) {
        return showtimeRepository.findById(id);
    }

    public Mono<Showtime> createShowtime(Showtime showtime) {
        return showtimeRepository.save(showtime);
    }

    public Mono<Void> deleteShowtimeById(UUID id) {
        return showtimeRepository.deleteById(id);
    }


}
