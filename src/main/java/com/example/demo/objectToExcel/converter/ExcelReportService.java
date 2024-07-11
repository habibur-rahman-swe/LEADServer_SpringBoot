package com.example.demo.objectToExcel.converter;

import java.io.FileNotFoundException;
import java.util.List;

import net.sf.jasperreports.engine.JRException;

public interface ExcelReportService {
	
	public <T> String exportReport(String reportFormat, List<T> employeeList) throws FileNotFoundException, JRException;
	
}
