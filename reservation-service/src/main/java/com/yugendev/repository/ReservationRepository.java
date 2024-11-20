package com.yugendev.repository;

import com.yugendev.model.Reservation;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.reactive.ReactorCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface ReservationRepository extends ReactorCrudRepository<Reservation, UUID> {

    Flux<Reservation> findByUserId(UUID userId);

    Mono<Reservation> findByReservationId(UUID reservationId);

}

