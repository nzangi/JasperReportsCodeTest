package com.jasperreportscodetest.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;



@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@Entity
//@Table(name = "employee_table")
@Table(name = "EMPLOYEE_TBL")

public class Employee {
//    @Id
//    @Column(name = "Id")
//    private int employeeId;
//    @Column(name = "name")
//    private String employeeName;
//    @Column(name = "department")
//    private String employeeDepartment;
//    @Column(name = "salary")
//    private double employeeSalary;
//    @Column(name = "hire_date")
//    private String employeeHireDate;


    @Id
    private int id;
    private String name;
    private String designation;
    private double salary;
    private String doj;
}
