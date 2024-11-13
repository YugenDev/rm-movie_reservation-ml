package com.yugendev.showtime_service.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue<String> showtimeQueue() {
        return new Queue<>("showtimeQueue", true);
    }
    
    @Bean
    public Queue reservationQueue() {
        return new Queue("reservationQueue", true);
    }

}
