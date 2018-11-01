package com.example.demo.database.demo.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "employee")
public class Employee implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGSERIAL")
    @JsonIgnore
    private long id;

    @Column(name = "name")
    @JsonProperty
    private String name;

    @Column(name = "employeeId")
    @JsonProperty
    private String employeeId;

    @JsonGetter
    public Long getId() {
        return id;
    }

    @JsonIgnore
    public void setId(Long uuid) {
        this.id = uuid;
    }

    public String getName() {
        return this.name;
    }

    public String getEmployeeId() {
        return this.employeeId;
    }


    public void setEmployeeId(String empId) {
        this.employeeId = empId;
    }


    public void getName(String name) {
        this.name = name;
    }

}
