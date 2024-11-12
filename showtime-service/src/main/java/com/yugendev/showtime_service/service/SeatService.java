package com.yugendev.showtime_service.service;

import com.yugendev.showtime_service.model.Seat;
import com.yugendev.showtime_service.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class SeatService {

    private final SeatRepository seatRepository;

    @Autowired
    public SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    public Mono<Seat> createSeat(Seat seat) {
        return seatRepository.save(seat);
    }

    public Flux<Seat> getSeatsByShowtimeId(UUID showtimeId) {
        return seatRepository.findAllByShowtimeId(showtimeId);
    }

    public Mono<Seat> getSeatByShowtimeIdAndSeatNumber(UUID showtimeId, String seatNumber) {
        return seatRepository.findByShowtimeIdAndSeatNumber(showtimeId, seatNumber);
    }


}
