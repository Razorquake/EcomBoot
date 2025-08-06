package com.razorquake.gateway;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
public class GatewayConfig {

    @Bean
    public RedisRateLimiter redisRateLimiter() {
        return new RedisRateLimiter(10, 20, 1);
    }

    @Bean
    public KeyResolver keyResolver() {
        return exchange -> Mono.just(
                exchange.getRequest().getRemoteAddress().getHostName()
        );
    }

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(
                        "product-service",
                        r -> r
                                .path("/api/products/**")
                                .filters(
                                        f -> f
                                                .requestRateLimiter(
                                                        config -> config
                                                                .setRateLimiter(redisRateLimiter())
                                                                .setKeyResolver(keyResolver())
                                                )
                                                .retry(10)
                                                .circuitBreaker(
                                                config -> config
                                                        .setName("ecomBreaker")
                                                        .setFallbackUri("forward:/fallback/Product")
                                        )
                                )
//                                .filters(f -> f.rewritePath(
//                                        "/products(?<segment>/?.*)",
//                                        "/api/products${segment}"
//                                ))
                                .uri("lb://product-service")

                )
                .route(
                        "user-service",
                        r -> r
                                .path("/api/users/**")
                                .filters(
                                        f -> f
                                                .requestRateLimiter(
                                                        config -> config
                                                                .setRateLimiter(redisRateLimiter())
                                                                .setKeyResolver(keyResolver())
                                                )
                                                .retry(10)
                                                .circuitBreaker(
                                                config -> config
                                                        .setName("ecomBreaker")
                                                        .setFallbackUri("forward:/fallback/User")
                                        )
                                )
//                                .filters(f -> f.rewritePath(
//                                        "/users(?<segment>/?.*)",
//                                        "/api/users${segment}"
//                                ))
                                .uri("lb://user-service")
                )
                .route(
                        "order-service",
                        r -> r
                                .path("/api/orders/**", "/api/cart/**")
                                .filters(
                                        f -> f
                                                .requestRateLimiter(
                                                        config -> config
                                                                .setRateLimiter(redisRateLimiter())
                                                                .setKeyResolver(keyResolver())
                                                )
                                                .retry(10)
                                                .circuitBreaker(
                                                config -> config
                                                        .setName("ecomBreaker")
                                                        .setFallbackUri("forward:/fallback/Order")
                                        )
                                )
//                                .filters(f -> f.rewritePath(
//                                        "/(?<segment>/?.*)",
//                                        "/api/${segment}"
//                                ))
                                .uri("lb://order-service")
                )
                .route(
                        "eureka-server",
                        r -> r
                                .path("/eureka/main")
                                .filters(
                                        f-> f
                                                .requestRateLimiter(
                                                        config -> config
                                                                .setKeyResolver(keyResolver())
                                                                .setRateLimiter(redisRateLimiter())
                                                )
                                                .retry(10)
                                                .setPath("/")
                                                .circuitBreaker(
                                                        config -> config
                                                                .setName("ecomBreaker")
                                                                .setFallbackUri("forward:/fallback/Eureka")
                                                )
                                )
                                .uri("http://localhost:8761")
                )
                .route(
                        "eureka-server-static",
                        r -> r
                                .path("/eureka/**")
                                .uri("http://localhost:8761")
                )
                .build();
    }

}
