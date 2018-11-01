package com.example.demo.services.imp;

import com.example.demo.database.demo.model.Employee;
import com.example.demo.database.demo.repository.EmployeeRepository;
import com.example.demo.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(value = "demoTransactionManager")
public class EmployeeServiceImp implements EmployeeService {

    @Autowired
    private EmployeeRepository repositories;

    public void create(Employee emp) {
        repositories.save(emp);
    }

    @Override
    public List<Employee> getAll() {
        return repositories.findAll(Pageable.unpaged()).getContent();
    }
}
