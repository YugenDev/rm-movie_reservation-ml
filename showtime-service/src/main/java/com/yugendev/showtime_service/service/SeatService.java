package com.yugendev.showtime_service.service;

import com.yugendev.showtime_service.model.Seat;
import com.yugendev.showtime_service.repository.SeatRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @Autowired
    public SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    public Flux<Seat> getSeatsByShowtimeId(UUID showtimeId) {
        return seatRepository.findAllByShowtimeId(showtimeId);
    }

    public Mono<Seat> getSeatByShowtimeIdAndSeatNumber(UUID showtimeId, String seatNumber) {
        return seatRepository.findByShowtimeIdAndSeatNumber(showtimeId, seatNumber);
    }

    // TODO: Implement rabbitMQ to notify other services when a seat is reserved
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
            });
    }

    public Mono<Void> deleteSeat(UUID showtimeId, String seatNumber) {
        return seatRepository.deleteByShowtimeIdAndSeatNumber(showtimeId, seatNumber);
    }

}
