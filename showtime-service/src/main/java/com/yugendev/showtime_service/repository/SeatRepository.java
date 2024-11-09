package com.yugendev.showtime_service.repository;

import com.yugendev.showtime_service.model.Seat;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface SeatRepository extends ReactiveCrudRepository<Seat, UUID> {

    Mono<Seat> findByShowtimeIdAndSeatNumber(UUID showtimeId, int seatNumber);
}
