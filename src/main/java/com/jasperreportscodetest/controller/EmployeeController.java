package com.jasperreportscodetest.controller;

import com.github.javafaker.Faker;
import com.jasperreportscodetest.entity.Address;
import com.jasperreportscodetest.entity.ContactInformation;
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
import java.util.Arrays;
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
    //list all the employees
    @GetMapping("/all-employees")
    public List<Employee> getAllEmployees(){
        return employeeRepository.findAll();
    }

    //Get employees based on the arguments given
    @GetMapping("/report/{format}")
    public String generateReport(@PathVariable String format,
                                 @RequestParam(required = false) String department,
                                 @RequestParam(required = false) Double minSalary,
                                 @RequestParam(required = false) Double maxSalary)
            throws FileNotFoundException, JRException {
        //filter report depending on the arguments given
        if (department == null || minSalary == null || maxSalary == null) {
            // If any parameter is missing, generate report without filtering
            return reportService.exportReport(format,null,0,0);
        } else {
            // Generate report with filtering based on parameters
            return reportService.exportReport(format, department, minSalary, maxSalary);
        }
    }

    //print salary report
    @GetMapping("/salary-report/{format}")
    public String generateSalaryReport(@PathVariable String format) throws JRException, FileNotFoundException {
        return reportService.exportSalaryReport(format);
    }

    //print chart report
    @GetMapping("/chart-report/{format}")
    public String generateChartReport(@PathVariable String format) throws JRException, FileNotFoundException {
        return reportService.exportChartReport(format);
    }

    //print address and contact report
    @GetMapping("/users-full-report/{format}")
    public String generateFullReport(@PathVariable String format) throws JRException, FileNotFoundException {
        return reportService.exportFullUSerReport(format);
    }
    // Generate dummpy data to the database
    @PostConstruct
    public void initializeData() {
        Faker faker = new Faker();
        List<String> departments = Arrays.asList("Engineering", "Marketing", "Sales", "Finance", "Operations", "Human Resources", "Customer Service");
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        List<Employee> fakeEmployees = IntStream.range(0, 10)
                .mapToObj(i -> {
                    Employee employee = new Employee();
                    employee.setEmployeeName(faker.name().fullName());
                    String randomDepartment = departments.get(faker.random().nextInt(departments.size()));
                    employee.setEmployeeDepartment(randomDepartment);
                    employee.setEmployeeSalary(faker.number().randomDouble(2, 3000, 10000));
//                    employee.setEmployeeHireDate(String.valueOf(Date.valueOf(sdf.format(faker.date()))));
                    // Generate fake address
                    Address address = new Address();
                    address.setStreet(faker.address().streetAddress());
                    address.setCity(faker.address().city());
                    address.setZipcode(faker.address().zipCode());
                    employee.setAddress(address);

                    // Generate fake contact information
                    ContactInformation contactInformation = new ContactInformation();
                    contactInformation.setPhoneNumber(faker.phoneNumber().cellPhone());
                    contactInformation.setEmail(faker.internet().emailAddress());
                    employee.setContactInformation(contactInformation);

                    LocalDateTime hireDateTime = faker.date().past(3650, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                    employee.setEmployeeHireDate(LocalDate.from(hireDateTime));
                    return employee;
                })
                .collect(Collectors.toList());

//        employeeRepository.saveAll(fakeEmployees);
    }


}
