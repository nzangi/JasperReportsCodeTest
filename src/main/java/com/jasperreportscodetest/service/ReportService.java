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

        // Load and Compile Reports
        File file = ResourceUtils.getFile("classpath:EmployeeReport.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

        // Data Sources
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(employees);

        // Parameters
        Map<String, Object> parameters = new HashMap<>();

        parameters.put("CreatedBy", "Nzangi Muoki");
        //Compile to generate report
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        if (reportFormat.equalsIgnoreCase("html")) {
            JasperExportManager.exportReportToHtmlFile(jasperPrint, path + "EmployeeReport.html");
        }
        else if (reportFormat.equalsIgnoreCase("pdf")) {
            JasperExportManager.exportReportToPdfFile(jasperPrint, path + "EmployeeReport.pdf");
        }


        return "The report was generated in : " + path;
    }
    //   generate combined department salary report
    public String exportSalaryReport(String reportFormat) throws FileNotFoundException, JRException {
        String path = "../JasperReportsCodeTest/JasperReports/";
        List<Employee>  employees = employeeRepository.findAll();

        File subreportFile = ResourceUtils.getFile("classpath:DepartmentSummary.jrxml");
        JasperReport subreportJasperReport = JasperCompileManager.compileReport(subreportFile.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(employees);

        // Parameters
        Map<String, Object> parameters = new HashMap<>();

        // Grouping employees by department and calculating total salary for each department
        Map<String, Double> departmentTotalSalaryMap = employees.stream()
                .collect(Collectors.groupingBy(Employee::getEmployeeDepartment, Collectors.summingDouble(Employee::getEmployeeSalary)));

        System.out.println("Department TotalSalary Map");
        System.out.println(departmentTotalSalaryMap);
        //parameters
        parameters.put("departmentTotalSalaryMap", departmentTotalSalaryMap);

        parameters.put("CreatedBy", "Nzangi Muoki");
        //Generate report
        JasperPrint jasperPrint = JasperFillManager.fillReport(subreportJasperReport, parameters, dataSource);
        if (reportFormat.equalsIgnoreCase("html")) {
            JasperExportManager.exportReportToHtmlFile(jasperPrint, path + "DepartmentSummary.html");
        }
        if (reportFormat.equalsIgnoreCase("pdf")) {
            JasperExportManager.exportReportToPdfFile(jasperPrint, path + "DepartmentSummary.pdf");
        }

        return "The report was generated in : " + path;
    }

    //Generate chart report
    public String exportChartReport(String reportFormat) throws FileNotFoundException, JRException {

        String path = "../JasperReportsCodeTest/JasperReports/";
        List<Employee> employees = employeeRepository.findAll();

        // Load and Compile Reports
        File subreportFile = ResourceUtils.getFile("classpath:ReportChart.jrxml");
        JasperReport chartReport = JasperCompileManager.compileReport(subreportFile.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(employees);

        // Prepare chart data
        Map<String, Long> departmentCounts = employees.stream()
                .filter(employee -> employee.getEmployeeDepartment() != null) // Filter out null departments
                .collect(Collectors.groupingBy(Employee::getEmployeeDepartment, Collectors.counting()));
        // Parameters
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("CreatedBy", "Nzangi Muoki");
        parameters.put("departmentCounts", departmentCounts);
        System.out.println("DepartmentCounts");
        System.out.println(departmentCounts);

        // Fill report
        JasperPrint jasperPrint = JasperFillManager.fillReport(chartReport, parameters,dataSource);

        if (reportFormat.equalsIgnoreCase("html")) {
            JasperExportManager.exportReportToHtmlFile(jasperPrint, path + "ReportChart.html");
        }
        if (reportFormat.equalsIgnoreCase("pdf")) {
            JasperExportManager.exportReportToPdfFile(jasperPrint, path + "ReportChart.pdf");
        }

        return "The report was generated in: " + path;
    }
    public String exportFullUSerReport(String reportFormat) throws FileNotFoundException, JRException {

        String path = "../JasperReportsCodeTest/JasperReports/";
        List<Employee> employees = employeeRepository.findAll();

        // Load and Compile Reports
        File subreportFile = ResourceUtils.getFile("classpath:EmployeeDetailsSubreport.jrxml");
        JasperReport employeeFullReport = JasperCompileManager.compileReport(subreportFile.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(employees);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("CreatedBy", "Nzangi Muoki");

        // Fill report
        JasperPrint jasperPrint = JasperFillManager.fillReport(employeeFullReport, parameters,dataSource);

        if (reportFormat.equalsIgnoreCase("html")) {
            JasperExportManager.exportReportToHtmlFile(jasperPrint, path + "EmployeeDetailsSubreport.html");
        }
        if (reportFormat.equalsIgnoreCase("pdf")) {
            JasperExportManager.exportReportToPdfFile(jasperPrint, path + "EmployeeDetailsSubreport.pdf");
        }
        return "The report was generated in: " + path;
    }

}
