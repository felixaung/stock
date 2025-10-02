package com.example.project_stock.serviceImplementation;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.project_stock.model.User;
import com.example.project_stock.repository.UserRepository;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;




@Service
public class ReportService {

    @Autowired
    private UserRepository userRepository;

    public byte[] exportReport() throws JRException {
        List<User> employees = userRepository.findAll();

        // Load JRXML from resources/reports/
        InputStream reportStream = getClass().getResourceAsStream("/reports/users_report.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

        // DataSource
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(employees);

        // Parameters (optional)
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Spring Boot Jasper Demo");

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
}