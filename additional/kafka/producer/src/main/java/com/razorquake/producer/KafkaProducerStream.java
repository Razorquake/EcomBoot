package com.razorquake.producer;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;
import java.util.function.Supplier;

@Configuration
public class KafkaProducerStream {

    @Bean
    public Supplier<RiderLocation> riderLocationSupplier() {
        Random random = new Random();
        String riderId = "rider"+random.nextInt(200);
        return () -> new RiderLocation(riderId, 12.9653, 77.5955);
    }
}
