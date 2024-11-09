package com.yugendev.showtime_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("showtimes")
public class Showtime {

    @Id
    private UUID showtimeId;

    private UUID movieId;
    private LocalDateTime showtimeDate;
    private int capacity;
    private int reservedSeats;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
