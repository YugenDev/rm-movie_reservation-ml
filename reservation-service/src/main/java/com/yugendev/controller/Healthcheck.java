package com.yugendev.controller;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

@Controller("/healthcheck")
public class Healthcheck {

    @Get(produces = "text/plain")
    public String healthcheck() {
        return "Reservation Service is up!!! 🎫🚀";
    }
}
