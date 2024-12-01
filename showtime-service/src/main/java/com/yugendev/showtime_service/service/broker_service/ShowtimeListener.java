package com.yugendev.showtime_service.service.broker_service;

import org.springframework.stereotype.Service;

import com.yugendev.showtime_service.service.ShowtimeService;

@Service
public class ShowtimeListener {

    private final ShowtimeService showtimeService;

    public ShowtimeListener(ShowtimeService showtimeService) {
        this.showtimeService = showtimeService;
    }


}
