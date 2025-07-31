package com.demo.consumer.resttemplate;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rest-template")
@RequiredArgsConstructor
public class RestTemplateController {

    private final RestTemplateClient restTemplateClient;

    @GetMapping("/instance")
    public String getInstance(){
//        RestTemplate restTemplate = new RestTemplate();
//        return restTemplate.getForObject(
//                "http://localhost:8081/instance-info",
//                String.class
//        );
        return restTemplateClient.getInstanceInfo();
    }
}
