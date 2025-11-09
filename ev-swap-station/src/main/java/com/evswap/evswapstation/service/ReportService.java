package com.evswap.evswapstation.service;

import com.evswap.evswapstation.entity.Report;
import com.evswap.evswapstation.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    // Thêm báo cáo mới
    public Report addReport(Report report) {
        return reportRepository.save(report);
    }

    // Cập nhật trạng thái của báo cáo
    public Report updateReportStatus(Long reportId, String status) {
        Optional<Report> reportOptional = reportRepository.findById(reportId);
        if (reportOptional.isPresent()) {
            Report report = reportOptional.get();
            report.setStatus(status);
            return reportRepository.save(report);
        }
        return null;  // Trả về null nếu không tìm thấy báo cáo
    }
}