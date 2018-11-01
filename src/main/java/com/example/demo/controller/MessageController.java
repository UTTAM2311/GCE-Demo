package com.example.demo.controller;

import com.example.demo.services.StorageService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@RestController
public class MessageController {

    @Autowired
    private StorageService storageService;
    @GetMapping(path = "/hello", produces = {MediaType.TEXT_PLAIN_VALUE})
    public ResponseEntity<String> message() {
        return ResponseEntity.ok("Hello World !");
    }

    @GetMapping(path = "/health", produces = {MediaType.TEXT_PLAIN_VALUE})
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("I am Healthy !");
    }

    @PostMapping(path = "/upload", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<?> upload(@ApiParam(name = "file", value = "Select the file to Upload", required = true) @RequestPart(value = "file", required = true) MultipartFile file) {
        try {
            InputStream stream = file.getInputStream();
            String uploadUrl = storageService.upload(stream, file.getOriginalFilename());
            System.out.println(uploadUrl);
        } catch (Exception e) {
            throw new RuntimeException("Upload failed", e);
        }
        return ResponseEntity.ok().build();
    }
}
