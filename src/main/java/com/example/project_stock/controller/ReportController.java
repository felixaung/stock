package com.example.project_stock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.project_stock.serviceImplementation.ReportService;

import net.sf.jasperreports.engine.JRException;

@Controller
public class ReportController {

    @Autowired
    private ReportService reportService;

    // Show Thymeleaf page with button
    @GetMapping("/report-page")
    public String reportPage(Model model) {
        return "/utils/report"; // resources/templates/report.html
    }

    // Generate and download PDF
    @GetMapping("/download-report")
    public ResponseEntity<byte[]> downloadReport() throws JRException {
        byte[] pdfBytes = reportService.exportReport();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}