package com.example.demo;

import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.demo.objectToExcel.converter.ExcelUtilService;
import com.example.demo.objectToExcel.converter.Person;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

	@Autowired
	private ExcelUtilService excelUtilService;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		try {
			List<String> headers = List.of("Name", "Age", "Email");
			List<Person> people = List.of(new Person("John Doe", 30, "john@example.com"),
					new Person("Jane Smith", 25, "jane@example.com"));

			Workbook workbook = excelUtilService.createExcelFile(headers, people);
			excelUtilService.saveExcelFile(workbook, "./src/main/resources/excel/people.xlsx");
			workbook.close();
		} catch (IOException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
