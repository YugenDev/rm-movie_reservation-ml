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
    private String seatNumber;
    private boolean isReserved;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Used to make reservations
    public Seat(UUID showtimeId, String seatNumber) {
        this.showtimeId = showtimeId;
        this.seatNumber = seatNumber;
        this.isReserved = false;  
        this.createdAt = LocalDateTime.now();  
        this.updatedAt = this.createdAt;
    }

}
