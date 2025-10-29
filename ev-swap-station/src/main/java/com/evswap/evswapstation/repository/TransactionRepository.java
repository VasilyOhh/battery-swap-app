package com.evswap.evswapstation.repository;

import com.evswap.evswapstation.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
        List<Transaction> findByUser_UserID(Integer userId);
        List<Transaction> findByStation_StationID(Integer stationId);
        List<Transaction> findByPackagePlan_Id(Integer id);
}
