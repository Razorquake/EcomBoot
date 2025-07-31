package com.demo.consumer.resttemplate;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class RestTemplateClient {

    private final RestTemplate restTemplate;
    private static final String PROVIDER_URL = "lb://producer";

    public String getInstanceInfo() {
        return restTemplate.getForObject(
                PROVIDER_URL + "/instance-info",
                String.class
        );
    }
}
