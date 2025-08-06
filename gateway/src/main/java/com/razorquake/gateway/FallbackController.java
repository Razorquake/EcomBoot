package com.razorquake.gateway;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
public class FallbackController {
    @GetMapping("/fallback/{name}")
    public ResponseEntity<List<String>> serviceUnavailable(@PathVariable String name) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Collections.singletonList(name+" service is Unavailable. Kindly try again later."));
    }
}
