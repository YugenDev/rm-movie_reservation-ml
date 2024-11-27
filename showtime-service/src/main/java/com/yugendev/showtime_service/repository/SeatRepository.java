package com.yugendev.showtime_service.repository;

import com.yugendev.showtime_service.model.Seat;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Repository
public interface SeatRepository extends ReactiveCrudRepository<Seat, UUID> {

    Mono<Seat> findByShowtimeIdAndSeatNumber(UUID showtimeId, String seatNumber);

    Flux<Seat> findAllByShowtimeId(UUID showtimeId);

    Flux<Seat> findAllByShowtimeIdAndSeatNumberIn(UUID showtimeId, List<String> seatNumbers);
}
