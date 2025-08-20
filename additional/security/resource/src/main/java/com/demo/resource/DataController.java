package com.demo.resource;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataController {
    @GetMapping("/api/home")
    public String getData(@AuthenticationPrincipal Jwt jwt) {
        String name = jwt.getClaimAsString("preferred_username");
        return "Hello, " + name;
    }
}
