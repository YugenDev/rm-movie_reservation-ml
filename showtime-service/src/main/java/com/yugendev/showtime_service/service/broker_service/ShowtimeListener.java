package com.yugendev.showtime_service.service.broker_service;

import org.springframework.stereotype.Service;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import com.yugendev.showtime_service.service.ShowtimeService;
import com.yugendev.showtime_service.service.broker_service.message.ReservationMessage;

@Service
public class ShowtimeListener {

    private final ShowtimeService showtimeService;

    public ShowtimeListener(ShowtimeService showtimeService) {
        this.showtimeService = showtimeService;
    }

    @RabbitListener(queues = "reservationQueue")
    public void receiveMessage(ReservationMessage message) {
        showtimeService.reserveSeats(message.getShowtimeId(), message.getSeatNumbers())
        .subscribe();
    }


}
