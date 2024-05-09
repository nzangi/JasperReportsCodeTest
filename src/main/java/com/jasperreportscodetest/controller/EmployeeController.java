package com.jasperreportscodetest.controller;

import com.github.javafaker.Faker;
import com.jasperreportscodetest.entity.Employee;
import com.jasperreportscodetest.repository.EmployeeRepository;
import com.jasperreportscodetest.service.ReportService;
import jakarta.annotation.PostConstruct;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ReportService reportService;

    @GetMapping("/all-employees")
    public List<Employee> getAllEmployees(){
        return employeeRepository.findAll();
    }

    @GetMapping("/report/{format}")
    public String generateReport(@PathVariable String format,
                                 @RequestParam(required = false) String department,
                                 @RequestParam(required = false) Double minSalary,
                                 @RequestParam(required = false) Double maxSalary)
            throws FileNotFoundException, JRException {
        if (department == null || minSalary == null || maxSalary == null) {
            // If any parameter is missing, generate report without filtering
            return reportService.exportReport(format,null,0,0);
        } else {
            // Generate report with filtering based on parameters
            return reportService.exportReport(format, department, minSalary, maxSalary);
        }
    }

    @GetMapping("/salary-report/{format}")
    public String generateSalaryReport(@PathVariable String format) throws JRException, FileNotFoundException {
        return reportService.exportSalaryReport(format);
    }
    @PostConstruct
    public void initializeData() {
        Faker faker = new Faker();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        List<Employee> fakeEmployees = IntStream.range(0, 10)
                .mapToObj(i -> {
                    Employee employee = new Employee();
                    employee.setEmployeeName(faker.name().fullName());
                    employee.setEmployeeDepartment(faker.company().profession());
                    employee.setEmployeeSalary(faker.number().randomDouble(2, 3000, 10000));
//                    employee.setEmployeeHireDate(String.valueOf(Date.valueOf(sdf.format(faker.date()))));
                    LocalDateTime hireDateTime = faker.date().past(3650, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                    employee.setEmployeeHireDate(LocalDate.from(hireDateTime));
                    return employee;
                })
                .collect(Collectors.toList());

//        employeeRepository.saveAll(fakeEmployees);
    }





}
