package com.razorquake.notification;

import com.razorquake.notification.payload.OrderCreatedEvent;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@CommonsLog
@Service
public class OrderEventConsumer {
//    @RabbitListener(queues = "${rabbitmq.queue.name}")
//    public void handleOrderEvent(OrderCreatedEvent orderEvent){
//        System.out.println("Received Message : " + orderEvent);
//        String message = "Order Placed Successfully. User Id : "
//                + orderEvent.getUserId()
//                + " Order Id : " + orderEvent.getOrderId()
//                + " Status : " + orderEvent.getStatus()
//                + " Total Amount : " + orderEvent.getTotalAmount()
//                + " Item List : " + orderEvent.getItem()
//                + " Created At : " + orderEvent.getCreatedAt();
//        System.out.println(message);
//        //Update the database
//        // Send email
//        // Generate Invoice
//        // Send Notification
//    }
    @Bean
    public Consumer<OrderCreatedEvent> createOrder() {
        return orderEvent -> {
            log.info("Received Message : " + orderEvent);
            System.out.println("Received Message : " + orderEvent);
            String message = "Order Placed Successfully. User Id : "
                    + orderEvent.getUserId()
                    + " Order Id : " + orderEvent.getOrderId()
                    + " Status : " + orderEvent.getStatus()
                    + " Total Amount : " + orderEvent.getTotalAmount()
                    + " Item List : " + orderEvent.getItem()
                    + " Created At : " + orderEvent.getCreatedAt();
            System.out.println(message);
        };
    }
}
