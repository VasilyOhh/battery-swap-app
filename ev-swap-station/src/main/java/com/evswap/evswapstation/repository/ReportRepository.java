package com.evswap.evswapstation.repository;

import com.evswap.evswapstation.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    // Bạn có thể thêm các truy vấn tuỳ chỉnh nếu cần
}
