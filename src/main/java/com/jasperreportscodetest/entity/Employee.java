package com.jasperreportscodetest.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Date;
import java.time.LocalDate;

// Annotating with @Entity indicates that this class is a JPA entity

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@Entity
//@Table(name = "employee_table")
// Specifying the table name for the entity
@Table(name = "employee_table")
//Create Table
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int employeeId;
    @Column(name = "name")
    private String employeeName;
    @Column(name = "department")
    private String employeeDepartment;
    @Column(name = "salary")
    private double employeeSalary;
    @Column(name = "hire_date")
    private LocalDate employeeHireDate;

//    @Id
//    private int id;
//    private String name;
//    private String designation;
//    private double salary;
//    private String doj;
}
