package com.example.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.demo.objectToExcel.converter.Employee;
import com.example.demo.objectToExcel.converter.ExcelReportService;
import com.example.demo.objectToExcel.converter.ExcelUtilService;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

	@Autowired
	private ExcelUtilService excelUtilService;

	@Autowired
	private ExcelReportService excelReportService;
	
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		try {
			List<String> headers = List.of("Id", "Name", "Designation", "Salary", "DateOfJoin");
			List<Employee> employees = List.of(new Employee(1L, "A", "A", 1.00, "01-02"),
											new Employee(2L, "B", "B", 2.00, "02-02"),
											new Employee(3L, "C", "C", 3.00, "03-02"),
											new Employee(4L, "D", "D", 4.00, "04-02"),
											new Employee(5L, "E", "E", 5.00, "05-02"));

			excelReportService.exportReport("html", employees);
			excelReportService.exportReport("pdf", employees);
			excelReportService.exportReport("xlsx", employees);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
