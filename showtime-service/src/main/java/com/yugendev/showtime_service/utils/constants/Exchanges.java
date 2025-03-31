package com.yugendev.showtime_service.utils.constants;

import lombok.Getter;

@Getter
public enum Exchanges {

    SEAT_RESERVATION_EXCHANGE("seat-reservation-exchange"),;

    private final String value;

    Exchanges(String value) {
        this.value = value;
    }

}
