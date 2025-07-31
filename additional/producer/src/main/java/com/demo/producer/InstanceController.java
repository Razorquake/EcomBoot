package com.demo.producer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class InstanceController {
    @Value("${server.port}")
    private String port;
    private final String instanceId = UUID.randomUUID().toString();
    @GetMapping("/instance-info")
    public String getInstanceInfo(){
        System.out.println(port);
        return "Instance served by port: " + port + ". Instance ID: " + instanceId;
    }
}
