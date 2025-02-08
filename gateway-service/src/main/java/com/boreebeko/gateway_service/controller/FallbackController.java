package com.boreebeko.gateway_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {

    @GetMapping(value = "/fallback")
    public ResponseEntity<String> fallback() {
        System.out.println("Fallback forward!");
        return new ResponseEntity<>("Forwarder to fallback", HttpStatus.SERVICE_UNAVAILABLE);
    }
}
