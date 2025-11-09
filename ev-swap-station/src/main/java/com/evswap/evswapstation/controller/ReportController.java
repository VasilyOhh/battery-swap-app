package com.evswap.evswapstation.controller;

import com.evswap.evswapstation.entity.Report;
import com.evswap.evswapstation.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    // API để thêm báo cáo mới
    @PostMapping
    public ResponseEntity<Report> addReport(@RequestBody Report report) {
        Report newReport = reportService.addReport(report);
        return new ResponseEntity<>(newReport, HttpStatus.CREATED);
    }

    // API để cập nhật trạng thái báo cáo
    @PutMapping("/{reportId}")
    public ResponseEntity<Report> updateReportStatus(@PathVariable Long reportId, @RequestBody String status) {
        Report updatedReport = reportService.updateReportStatus(reportId, status);
        if (updatedReport != null) {
            return new ResponseEntity<>(updatedReport, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Nếu không tìm thấy báo cáo
        }
    }
}