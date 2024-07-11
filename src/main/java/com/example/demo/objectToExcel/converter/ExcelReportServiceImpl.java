package com.example.demo.objectToExcel.converter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

@Service
public class ExcelReportServiceImpl implements ExcelReportService {

	@Override
	public <T> String exportReport(String reportFormat, List<T> employeeList)
            throws FileNotFoundException, JRException {

        File file = ResourceUtils.getFile("classpath:jesper-excel/employees.jrxml");
        String path = "./src/main/resources/excel/";
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(employeeList);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Java Techie");

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        if (reportFormat.equalsIgnoreCase("html")) {
            JasperExportManager.exportReportToHtmlFile(jasperPrint, path + "employees.html");
        }

        if (reportFormat.equalsIgnoreCase("pdf")) {
            try {
                JasperExportManager.exportReportToPdfFile(jasperPrint, path + "employees.pdf");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (reportFormat.equalsIgnoreCase("xlsx")) {
            try {
                JRXlsxExporter exporter = new JRXlsxExporter();
                exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(path + "employees.xlsx"));

                SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
                configuration.setOnePagePerSheet(true);
                exporter.setConfiguration(configuration);

                exporter.exportReport();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return "Report generated in path: " + path;
    }

}
