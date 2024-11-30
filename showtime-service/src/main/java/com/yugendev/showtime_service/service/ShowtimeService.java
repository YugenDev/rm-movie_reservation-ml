package com.yugendev.showtime_service.service;

import com.yugendev.showtime_service.model.Seat;
import com.yugendev.showtime_service.model.Showtime;
import com.yugendev.showtime_service.repository.SeatRepository;
import com.yugendev.showtime_service.repository.ShowtimeRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ShowtimeService {

    private static final Logger logger = LoggerFactory.getLogger(ShowtimeService.class);

    private final ShowtimeRepository showtimeRepository;
    private final SeatRepository seatRepository;
    private final RabbitTemplate rabbitTemplate;


    @Autowired
    public ShowtimeService(ShowtimeRepository showtimeRepository, SeatRepository seatRepository, RabbitTemplate rabbitTemplate) {
        this.showtimeRepository = showtimeRepository;
        this.seatRepository = seatRepository;
        this.rabbitTemplate = rabbitTemplate;
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
            );
    }

private Mono<Void> createSeatsForShowtime(Showtime showtime) {
    List<String> seatPrefixes = Arrays.asList("A", "B", "C", "D", "E", "F", "G");

    Flux<Seat> seats = Flux.fromStream(
        IntStream.range(0, seatPrefixes.size())
            .boxed()
            .flatMap(prefixIndex ->
                IntStream.range(1, 11)
                    .mapToObj(i -> {
                        Seat seat = new Seat();
                        seat.setShowtimeId(showtime.getShowtimeId());
                        seat.setSeatNumber(seatPrefixes.get(prefixIndex) + i);
                        seat.setReserved(false);
                        return seat;
                    })
            )
    );

    return seatRepository.saveAll(seats).then();
}

    public Mono<Showtime> createShowtime(Showtime showtime) {
        return showtimeRepository.save(showtime)
                .flatMap(savedShowtime -> createSeatsForShowtime(savedShowtime)
                        .then(Mono.just(savedShowtime)))
                .doOnSuccess(savedShowtime ->
                        rabbitTemplate.convertAndSend("showtimeExchange", "showtime", savedShowtime)
                );
    }

    public Mono<Void> deleteShowtimeById(UUID id) {
        return showtimeRepository.deleteById(id);
    }

    public Mono<Showtime> updateShowtime(UUID id, Showtime showtime) {
        return showtimeRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Showtime not found")))
                .flatMap(existingShowtime -> {
                    existingShowtime.setMovieId(showtime.getMovieId());
                    existingShowtime.setShowtimeDate(showtime.getShowtimeDate());
                    existingShowtime.setCapacity(showtime.getCapacity());
                    return showtimeRepository.save(existingShowtime);
                });
    }

}
