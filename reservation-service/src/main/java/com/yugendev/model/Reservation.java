package com.yugendev.model;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Data
@Introspected
@MappedEntity
public class Reservation {

    @Id
    @GeneratedValue
    private UUID reservationId;
    private UUID userId;
    private UUID showtimeId;
    private Map<String, Object> reservedSeats;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
