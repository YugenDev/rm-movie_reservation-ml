package com.yugendev.showtime_service.service;

import com.yugendev.showtime_service.model.Seat;
import com.yugendev.showtime_service.model.Showtime;
import com.yugendev.showtime_service.repository.SeatRepository;
import com.yugendev.showtime_service.repository.ShowtimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ShowtimeService {

    private static final Logger logger = LoggerFactory.getLogger(ShowtimeService.class);

    private final ShowtimeRepository showtimeRepository;
    private final SeatRepository seatRepository;


    @Autowired
    public ShowtimeService(ShowtimeRepository showtimeRepository, SeatRepository seatRepository) {
        this.showtimeRepository = showtimeRepository;
        this.seatRepository = seatRepository;
    }

    public Flux<Showtime> getAllShowtimes() {
        return showtimeRepository.findAll();
    }

    public Mono<Showtime> getShowtimeById(UUID id) {
        return showtimeRepository.findById(id);
    }

    public Flux<Seat> getAvalibleSeats(UUID showtimeId) {
        return seatRepository.findAllByShowtimeId(showtimeId)
        .filter(seat -> !seat.isReserved());
    }

    public Flux<Seat> getAllSeats(UUID showtimeId) {
        return seatRepository.findAllByShowtimeId(showtimeId);
    }

    // reserveSeats method
    public Mono<List<Seat>> reserveSeats(UUID showtimeId, List<String> seatNumbers) {
        logger.debug("Reserving seats for showtimeId: {} with seatNumbers: {}", showtimeId, seatNumbers);

        // Input validation
        if (showtimeId == null) {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Showtime ID must be provided"));
        }
        if (seatNumbers == null || seatNumbers.isEmpty()) {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Seat numbers list cannot be empty"));
        }

        // Remove duplicates and validate seat number format
        List<String> uniqueSeats = seatNumbers.stream()
            .distinct()
            .collect(Collectors.toList());

        if (uniqueSeats.size() != seatNumbers.size()) {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                "Duplicate seat numbers are not allowed in the reservation"));
        }

        // Validate seat number format
        List<String> invalidSeats = uniqueSeats.stream()
            .filter(seatNum -> !seatNum.matches("^[A-Z]\\d+$"))
            .collect(Collectors.toList());

        if (!invalidSeats.isEmpty()) {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                "Invalid seat number format for seats: " + String.join(", ", invalidSeats)));
        }

        return showtimeRepository.findById(showtimeId)
            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, 
                "Showtime with ID " + showtimeId + " not found")))
            .flatMap(showtime -> 
                seatRepository.findAllByShowtimeIdAndSeatNumberIn(showtimeId, uniqueSeats)
                    .collectList()
                    .flatMap(seats -> {
                        if (seats.size() != uniqueSeats.size()) {
                            List<String> foundSeats = seats.stream()
                                .map(Seat::getSeatNumber)
                                .collect(Collectors.toList());
                            List<String> notFoundSeats = uniqueSeats.stream()
                                .filter(seat -> !foundSeats.contains(seat))
                                .collect(Collectors.toList());
                            return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, 
                                "Seats not found: " + String.join(", ", notFoundSeats)));
                        }

                        List<Seat> reservedSeats = seats.stream()
                            .filter(Seat::isReserved)
                            .collect(Collectors.toList());

                        if (!reservedSeats.isEmpty()) {
                            String reservedSeatNumbers = reservedSeats.stream()
                                .map(Seat::getSeatNumber)
                                .collect(Collectors.joining(", "));
                            return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, 
                                "Cannot complete reservation. Seats already reserved: " + reservedSeatNumbers));
                        }

                        return seatRepository.saveAll(seats.stream()
                            .peek(seat -> seat.setReserved(true))
                            .collect(Collectors.toList()))
                            .collectList();
                    })
            )
            .onErrorMap(ex -> {
                if (ex instanceof ResponseStatusException) {
                    return ex;
                }
                logger.error("Unexpected error during seat reservation", ex);
                return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                    "An unexpected error occurred while processing your reservation");
            });
    }

    public Mono<Seat> reserveSingleSeat(UUID showtimeId, String seatNumber) {
        logger.debug("Reserving single seat for showtimeId: {} with seatNumber: {}", showtimeId, seatNumber);

        if (showtimeId == null || seatNumber == null || !seatNumber.matches("^[A-Z]\\d+$")) {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid showtime ID or seat number format"));
        }

        return showtimeRepository.findById(showtimeId)
            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Showtime not found")))
            .flatMap(showtime -> 
                seatRepository.findByShowtimeIdAndSeatNumber(showtimeId, seatNumber)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Seat not found")))
                    .flatMap(seat -> {
                        if (seat.isReserved()) {
                            return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "Seat is already reserved"));
                        }
                        seat.setReserved(true);
                        return seatRepository.save(seat);
                    })
            )
            .onErrorMap(ex -> {
                logger.error("Error in single seat reservation", ex);
                return (ex instanceof ResponseStatusException) ? ex :
                    new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Reservation failed", ex);
            });
    }

    private Mono<Void> createSeatsForShowtime(Showtime showtime) {
        int capacity = showtime.getCapacity();
        String seatPrefix = "A";
        // TODO: Implement more prefixes with specific limits
        // and take this function to another module
    
        Flux<Seat> seats = Flux.fromStream(
            IntStream.range(1, capacity + 1)
                .mapToObj(i -> {
                    Seat seat = new Seat();
                    seat.setShowtimeId(showtime.getShowtimeId());
                    seat.setSeatNumber(seatPrefix + i);  
                    seat.setReserved(false);  
                    return seat;
                })
        );
    
        return seatRepository.saveAll(seats).then();
    }

    public Mono<Showtime> createShowtime(Showtime showtime) {
        return showtimeRepository.save(showtime)
                .flatMap(savedShowtime -> createSeatsForShowtime(savedShowtime)
                        .then(Mono.just(savedShowtime)));
    }

    public Mono<Void> deleteShowtimeById(UUID id) {
        return showtimeRepository.deleteById(id);
    }

}
