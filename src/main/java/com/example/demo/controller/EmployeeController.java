package com.example.demo.controller;

import com.example.demo.database.demo.model.Employee;
import com.example.demo.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService service;


    @PostMapping(path = "/create", produces = {MediaType.TEXT_PLAIN_VALUE})
    public ResponseEntity<String> create(@RequestBody Employee employee) {
        service.create(employee);
        return ResponseEntity.ok("created successfully");
    }

    @GetMapping(path = "/get", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<Employee>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

}
