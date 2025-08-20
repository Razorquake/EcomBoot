package com.razorquake.consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

//@Component
@Configuration
public class KafkaConsumer {
//    @KafkaListener(topics = "my-topic", groupId = "my-new-group-1")
//    public void listen1(String message) {
//        System.out.println("Message received 1: " + message);
//    }
//    @KafkaListener(topics = "my-topic-2", groupId = "my-new-group-2")
//    public void listen3(RiderLocation ride) {
//        System.out.println("Message received 2: " + ride);
//    }
    @Bean
    public Consumer<Message<RiderLocation>> consumer() {

        return message -> {
            RiderLocation riderLocation = message.getPayload();
            Integer partition = message.getHeaders().get(KafkaHeaders.RECEIVED_PARTITION, Integer.class);
            System.out.println("Message received from partition "+partition+": " + riderLocation);
        };
    }
}
