package com.example.demo.services;


import com.example.demo.database.demo.model.Employee;

import java.util.List;

public interface EmployeeService {
    void create(Employee emp);

    List<Employee> getAll();
}
