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

@Service
public class ShowtimeService {

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

    //TODO: IMPLEMENT THIS
    public Mono<List<Seat>> reserveSeats(UUID showtimeId, List<String> seatNumbers) {
        if (showtimeId == null || seatNumbers == null || seatNumbers.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Showtime ID and seat numbers must be provided");
        }
    
        List<Mono<Seat>> seatReservations = seatNumbers.stream()
                .map(seatNumber -> reserveSingleSeat(showtimeId, seatNumber))
                .collect(Collectors.toList());
    
        return Mono.zip(seatReservations, seats -> {
            return Arrays.stream(seats)
                    .map(Seat.class::cast)
                    .collect(Collectors.toList());
        })
        .onErrorMap(ex -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error reservando asientos", ex));
    }    

    public Mono<Seat> reserveSingleSeat(UUID showtimeId, String seatNumber) {
        if (showtimeId == null || seatNumber == null || seatNumber.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Showtime ID and seat number must be provided");
        }
    
        return seatRepository.findByShowtimeIdAndSeatNumber(showtimeId, seatNumber)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Seat not found for the given showtimeId and seatNumber")))
                .flatMap(seat -> {
                    if (seat.isReserved()) {
                        return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "Seat is already reserved"));
                    }
    
                    seat.setReserved(true);
                    return seatRepository.save(seat);
                })
                .onErrorResume(ex -> Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred when saving the seat", ex)));
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
