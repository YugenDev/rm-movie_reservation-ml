package com.yugendev.showtime_service.repository;

import com.yugendev.showtime_service.model.Showtime;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface ShowtimeRepository extends ReactiveCrudRepository<Showtime, UUID> {

    Mono<Showtime> findByShowtimeId(UUID showtimeId);
}
