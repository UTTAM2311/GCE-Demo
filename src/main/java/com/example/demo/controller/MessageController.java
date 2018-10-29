package com.example.demo.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {

    @GetMapping(path = "/hello", produces = {MediaType.TEXT_PLAIN_VALUE})
    public ResponseEntity<String> message() {
        return ResponseEntity.ok("Hello World !");
    }
}
