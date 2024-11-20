package com.yugendev.config;

import com.yugendev.repository.ReservationRepository;
import io.micronaut.data.annotation.RepositoryConfiguration;
import jakarta.inject.Singleton;

@RepositoryConfiguration
public class DataConfig {

    @Singleton
    public ReservationRepository reservationRepository(ReservationRepository reservationRepository) {
        return reservationRepository;
    }

}
