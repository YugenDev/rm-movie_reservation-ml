package com.yugendev.showtime_service.service;

import com.yugendev.showtime_service.model.Seat;
import com.yugendev.showtime_service.model.Showtime;
import com.yugendev.showtime_service.repository.SeatRepository;
import com.yugendev.showtime_service.repository.ShowtimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;
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

    private Mono<Void> createSeatsForShowtime(Showtime showtime) {
        int capacity = showtime.getCapacity();
        String seatPrefix = "A";
        // TODO: Implement more prefixes with specific limits
    
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
