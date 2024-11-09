package com.yugendev.showtime_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/healthcheck")
public class Healthcheck {

    @GetMapping
    public String healthcheck() {
        return "showtime service is running! 🎬🚀👌!";
    }
}
