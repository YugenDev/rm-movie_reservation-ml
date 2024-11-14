package com.yugendev.showtime_service.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue showtimeQueue() {
        return new Queue("showtimeQueue", true);
    }
    
    @Bean
    public Queue reservationQueue() {
        return new Queue("reservationQueue", true);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange("movieExchange");
    }

    @Bean
    public Binding showtimeBinding(Queue showtimeQueue, DirectExchange exchange) {
        return BindingBuilder.bind(showtimeQueue).to(exchange).with("showtime");
    }

    @Bean
    public Binding reservationBinding(Queue reservationQueue, DirectExchange exchange) {
        return BindingBuilder.bind(reservationQueue).to(exchange).with("reservation");
    }
}
