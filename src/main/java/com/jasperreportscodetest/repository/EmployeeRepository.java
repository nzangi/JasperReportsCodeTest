package com.jasperreportscodetest.repository;

import com.jasperreportscodetest.entity.Employee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.EntityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Integer> {
    // Method to find employees by department and salary range
    // It returns a list of employees whose department matches the provided department
    List<Employee> findByEmployeeDepartmentAndEmployeeSalaryBetween(String department, double minSalary, double maxSalary);

//    @Query("SELECT new com.jasperreportscodetest.entity.DepartmentSummary(e.employeeDepartment AS departmentName, SUM(e.employeeSalary) AS totalSalary) FROM Employee e GROUP BY e.employeeDepartment")
//    List<DepartmentSummary> findDepartmentTotals();



}

