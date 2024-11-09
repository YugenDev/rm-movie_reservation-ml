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
@Table("seats")
public class Seat {

    @Id
    private UUID seatId;

    private UUID showtimeId;
    private int seatNumber;
    private boolean isReserved;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
