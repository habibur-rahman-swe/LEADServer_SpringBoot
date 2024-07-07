package com.example.demo.objectToExcel.converter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

@Service
public class ExcelUtilServiceImpl implements ExcelUtilService {
	
	@Override
	public <T> Workbook createExcelFile(List<String> headers, List<T> data) throws IOException, IllegalAccessException {
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Sheet1");

		// First row with the text "Information Table"
        Row row1 = sheet.createRow(0);
        Cell cell1 = row1.createCell(0);
        cell1.setCellValue("Information Table");
        
        // Second row with the current date
        Row row2 = sheet.createRow(1);
        Cell cell2 = row2.createCell(0);
        cell2.setCellValue(LocalDate.now().toString());
        
        // Third row blank
        sheet.createRow(2);
		
		// Create header row
		Row headerRow = sheet.createRow(3);
		for (int i = 0; i < headers.size(); i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(headers.get(i));
		}

		// Create data rows
		for (int i = 0; i < data.size(); i++) {
			Row dataRow = sheet.createRow(i + 3);
			T item = data.get(i);
			Field[] fields = item.getClass().getDeclaredFields();

			for (int j = 0; j < headers.size(); j++) {
				Cell cell = dataRow.createCell(j);
				if (j < fields.length) {
					fields[j].setAccessible(true);
					Object value = fields[j].get(item);
					if (value != null) {
						cell.setCellValue(value.toString());
					}
				}
			}
		}

		return workbook;
	}

	@Override
	public void saveExcelFile(Workbook workbook, String filePath) throws IOException {
		try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
			workbook.write(fileOut);
		}
	}
}
