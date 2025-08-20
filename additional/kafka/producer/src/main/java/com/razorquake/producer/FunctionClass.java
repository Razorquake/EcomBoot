package com.razorquake.producer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class FunctionClass {
    @Bean
    public Function<String, String> uppercase() {
        return String::toUpperCase;
    }
}
