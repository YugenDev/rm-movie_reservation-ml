package com.yugendev.repository;

import com.yugendev.model.Reservation;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.reactive.ReactorCrudRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Repository
public interface ReservationRepository extends ReactorCrudRepository<Reservation, UUID> {

    Flux<Reservation> findByUserId(UUID userId);

    Flux<Reservation> findByShowtimeId(UUID showtimeId);

    Flux<Reservation> findByUserIdAndShowtimeId(UUID userId, UUID showtimeId);

}
