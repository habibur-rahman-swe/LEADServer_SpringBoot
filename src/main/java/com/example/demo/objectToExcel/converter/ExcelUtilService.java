package com.example.demo.objectToExcel.converter;

import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;

public interface ExcelUtilService {
	public <T> Workbook createExcelFile(List<String> headers, List<T> data) throws IOException, IllegalAccessException;

	public void saveExcelFile(Workbook workbook, String filePath) throws IOException;
}
