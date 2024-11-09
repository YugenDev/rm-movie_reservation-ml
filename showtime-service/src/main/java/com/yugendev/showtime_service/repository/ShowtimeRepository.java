package com.yugendev.showtime_service.repository;

import com.yugendev.showtime_service.model.Showtime;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ShowtimeRepository extends ReactiveCrudRepository<Showtime, UUID> {

    Mono<Showtime> findByShowtimeId(UUID showtimeId);
}
