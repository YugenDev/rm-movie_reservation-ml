package com.yugendev.showtime_service.service;

import com.yugendev.showtime_service.model.Seat;
import com.yugendev.showtime_service.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class SeatService {

    private final SeatRepository seatRepository;

    @Autowired
    public SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    public Mono<Seat> getSeatByShowtimeIdAndSeatNumber(UUID showtimeId, int seatNumber) {
        return seatRepository.findByShowtimeIdAndSeatNumber(showtimeId, seatNumber);
    }

    public Mono<Seat> createSeat(Seat seat) {
        return seatRepository.save(seat);
    }
}