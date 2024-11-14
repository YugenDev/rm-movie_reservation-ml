package com.yugendev.controller;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

@Controller
public class Healthcheck {

    @Get("/healthcheck")
    public String healthcheck() {
        return "Reservation Service is up!!! 🎫🚀";
    }
}
