package com.yugendev.model;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedEntity
@Introspected
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
