package com.evswap.evswapstation.repository;

import com.evswap.evswapstation.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByStation_StationID(Integer stationId);
    List<Feedback> findByUser_UserID(Integer userId);
}
