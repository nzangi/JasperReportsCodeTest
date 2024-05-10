package com.jasperreportscodetest.repository;

import com.jasperreportscodetest.entity.Employee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.EntityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
//JPA repository for fetching data in the MySQL Database
@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Integer> {
    // Method to find employees by department and salary range
    // It returns a list of employees whose department matches the provided department
    List<Employee> findByEmployeeDepartmentAndEmployeeSalaryBetween(String department, double minSalary, double maxSalary);

}

