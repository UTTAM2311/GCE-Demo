package com.example.demo.controller;

import com.example.demo.services.StorageService;
import com.example.demo.services.imp.PubSub;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@RestController
public class MessageController {

    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);


    @Value("${application.pubsub.subscription.upload}")
    private String subscriptionName;

    @Autowired
    private StorageService storageService;

    @Autowired
    public PubSub pub;

    @GetMapping(path = "/hello", produces = {MediaType.TEXT_PLAIN_VALUE})
    public ResponseEntity<String> message() {
        return ResponseEntity.ok("Hello World !");
    }

    @GetMapping(path = "/", produces = {MediaType.TEXT_PLAIN_VALUE})
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("I am Healthy !");
    }

    @PostMapping(path = "/upload", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> upload(@ApiParam(name = "file", value = "Select the file to Upload", required = true) @RequestPart(value = "file", required = true) MultipartFile file) {
        try {
            InputStream stream = file.getInputStream();
            String uploadUrl = storageService.upload(stream, file.getOriginalFilename());
            System.out.println(uploadUrl);
        } catch (Exception e) {
            throw new RuntimeException("Upload failed", e);
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/publishMessage")
    public ResponseEntity<?> publishMessage(@RequestParam("message") String message) {
        pub.publish(message);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/pullMessage", produces = {MediaType.TEXT_PLAIN_VALUE})
    public ResponseEntity<?> receiveMessage() {
        return ResponseEntity.ok(pub.receive());
    }

}
