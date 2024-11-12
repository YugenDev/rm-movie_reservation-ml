package com.yugendev.showtime_service.utils.exception;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class TechnicalError {

    public static final Mono<ServerResponse> response404 = ServerResponse.notFound().build();

    public static final Mono<ServerResponse> response400 = ServerResponse.badRequest().build();

    public static final Mono<ServerResponse> response406 = ServerResponse.status(HttpStatus.NOT_ACCEPTABLE).build();

    public static final Mono<ServerResponse> response500 = ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

    private TechnicalError() {

    }

}
