package com.yugendev.showtime_service.service;

import com.yugendev.showtime_service.model.Seat;
import com.yugendev.showtime_service.repository.SeatRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Service
public class SeatService {

    private static final Logger logger = LoggerFactory.getLogger(SeatService.class);

    private final SeatRepository seatRepository;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public SeatService(SeatRepository seatRepository, RabbitTemplate rabbitTemplate) {
        this.seatRepository = seatRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    public Flux<Seat> getSeatsByShowtimeId(UUID showtimeId) {
        return seatRepository.findAllByShowtimeId(showtimeId);
    }

    public Mono<Seat> getSeatByShowtimeIdAndSeatNumber(UUID showtimeId, String seatNumber) {
        return seatRepository.findByShowtimeIdAndSeatNumber(showtimeId, seatNumber);
    }

    public Mono<List<Seat>> reserveSeats(UUID showtimeId, List<String> seatNumbers) {
        return seatRepository.findAllByShowtimeIdAndSeatNumberIn(showtimeId, seatNumbers)
                .filter(seat -> !seat.isReserved())
                .collectList()
                .flatMap(seats -> {
                    if (seats.size() != seatNumbers.size()) {
                        return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "Some seats are already reserved"));
                    }
                    seats.forEach(seat -> seat.setReserved(true));
                    return seatRepository.saveAll(seats).collectList();
                })
                .doOnSuccess(seats -> {
                    logger.info("Successfully reserved seats: {}", seats);
                    rabbitTemplate.convertAndSend("seat-reservation-exchange", "seat-reservation-created", seats);
                })
                .onErrorMap(ex -> {
                    if (ex instanceof ResponseStatusException) {
                        return ex;
                    }
                    logger.error("Unexpected error during seat reservation", ex);
                    return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                            "An unexpected error occurred while processing your reservation");
                });
    }

    public Mono<Seat> unreserveSeat(UUID showtimeId, String seatNumber) {
        return seatRepository.findByShowtimeIdAndSeatNumber(showtimeId, seatNumber)
            .flatMap(seat -> {
                if (!seat.isReserved()) {
                    return Mono.error(new RuntimeException("Seat is not reserved"));
                }
                seat.setReserved(false);
                return seatRepository.save(seat);
            })
            .doOnSuccess(seat -> {
                logger.info("Successfully unreserved seat: {}", seat);
                rabbitTemplate.convertAndSend("seat-reservation-exchange", "seat-reservation-deleted", seat);
            });
    }

    public Mono<Void> deleteSeat(UUID showtimeId, String seatNumber) {
        return seatRepository.deleteByShowtimeIdAndSeatNumber(showtimeId, seatNumber)
                .doOnSuccess(aVoid -> {
                    logger.info("Successfully deleted seat with number {} for showtime {}", seatNumber, showtimeId);
                    rabbitTemplate.convertAndSend("seat-reservation-exchange", "seat-reservation-deleted", seatNumber);
                })
                .doOnError(ex -> logger.error("Error deleting seat with number {} for showtime {}: {}", seatNumber, showtimeId, ex.getMessage()));
    }

}
