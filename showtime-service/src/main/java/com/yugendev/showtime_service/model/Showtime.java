package com.yugendev.showtime_service.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
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

    @NotNull
    private UUID movieId;

    private LocalDateTime showtimeDate;

    @Min(1)
    private int capacity;

    @Min(0)
    private int reservedSeats;

    @PastOrPresent
    private LocalDateTime createdAt;

    @PastOrPresent
    private LocalDateTime updatedAt;

}
