package com.jasperreportscodetest.service;

import com.jasperreportscodetest.entity.Employee;
import com.jasperreportscodetest.repository.EmployeeRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import org.antlr.v4.runtime.tree.pattern.ParseTreePattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportService {
    // Dependency Injection EmployeeRepository (IoC)
    @Autowired
    private EmployeeRepository employeeRepository;
    //  Generate report according to parameters given
    public String exportReport(String reportFormat,String department, double minSalary, double maxSalary) throws FileNotFoundException, JRException {
        //location where the file is saved
        String path = "../JasperReportsCodeTest/JasperReports/";
        //  eldercare to store employees on List
        List<Employee> employees;
        // Fetch Data
        if(department== null || maxSalary == 0 || minSalary == 0){
            // Find All Employees

            employees = employeeRepository.findAll();
        }else {
            employees = employeeRepository.findByEmployeeDepartmentAndEmployeeSalaryBetween(department, minSalary, maxSalary);
        }

        System.out.println(employees);
        // Load and Compile Reports
        File file = ResourceUtils.getFile("classpath:EmployeeReport.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

        File subreportFile = ResourceUtils.getFile("classpath:DepartmentSummary.jrxml");
//        JasperReport subreportJasperReport = JasperCompileManager.compileReport(subreportFile.getAbsolutePath());
//        JasperReport subreportJasperReport = (JasperReport) JRLoader.loadObject(subreportFile);


        // Data Sources
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(employees);

        // Parameters
        Map<String, Object> parameters = new HashMap<>();


        //For filtering purposes,if the arguments are given
//        parameters.put("Department", department);
//        parameters.put("MinSalary", minSalary);
//        parameters.put("MaxSalary", maxSalary);

        parameters.put("CreatedBy", "Nzangi Muoki");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        if (reportFormat.equalsIgnoreCase("html")) {
            JasperExportManager.exportReportToHtmlFile(jasperPrint, path + "EmployeeReport.html");
        }
        if (reportFormat.equalsIgnoreCase("pdf")) {
            JasperExportManager.exportReportToPdfFile(jasperPrint, path + "EmployeeReport.pdf");
        }


        return "The report was generated in : " + path;
    }

    public String exportSalaryReport(String reportFormat) throws FileNotFoundException, JRException {
        String path = "../JasperReportsCodeTest/JasperReports/";
        List<Employee>  employees = employeeRepository.findAll();

        File subreportFile = ResourceUtils.getFile("classpath:DepartmentSummary.jrxml");
        JasperReport subreportJasperReport = JasperCompileManager.compileReport(subreportFile.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(employees);

        // Parameters
        Map<String, Object> parameters = new HashMap<>();
//        Map<String, Object> parameter = new HashMap<>();

//        Map<String, Object> parameter = new HashMap<>();


        // Grouping employees by department and calculating total salary for each department
        Map<String, Double> departmentTotalSalaryMap = employees.stream()
                .collect(Collectors.groupingBy(Employee::getEmployeeDepartment, Collectors.summingDouble(Employee::getEmployeeSalary)));

//        departmentTotalSalaryMap.forEach((departmentName, totalSalary) -> parameter.put(departmentName + "_totalSalary", totalSalary));
        System.out.println("Department TotalSalary Map");
        System.out.println(departmentTotalSalaryMap);

        parameters.put("departmentTotalSalaryMap", departmentTotalSalaryMap);
//        System.out.println("Parameter");

//        System.out.println(parameter);

        parameters.put("CreatedBy", "Nzangi Muoki");
        JasperPrint jasperPrint = JasperFillManager.fillReport(subreportJasperReport, parameters, dataSource);
        if (reportFormat.equalsIgnoreCase("html")) {
            JasperExportManager.exportReportToHtmlFile(jasperPrint, path + "EmployeeSalaryReport.html");
        }
        if (reportFormat.equalsIgnoreCase("pdf")) {
            JasperExportManager.exportReportToPdfFile(jasperPrint, path + "EmployeeSalaryReport.pdf");
        }

        return "The report was generated in : " + path;
    }

    }
