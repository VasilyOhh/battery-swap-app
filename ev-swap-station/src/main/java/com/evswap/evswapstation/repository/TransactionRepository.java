package com.evswap.evswapstation.repository;

import com.evswap.evswapstation.dto.TransactionBatteryDTO;
import com.evswap.evswapstation.entity.TransactionEntity;
import com.evswap.evswapstation.dto.TransactionDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    // Existing methods
    TransactionEntity findByPayPalTransactionId(String payPalTransactionId);
    List<TransactionEntity> findByStatus(String status);

    // QUERY ĐÃ SỬA - Sử dụng CONCAT để convert sang String
    @Query("SELECT new com.evswap.evswapstation.dto.TransactionDTO(" +
            "t.transactionId, t.timeDate, u.fullName, u.email, v.vin, " +
            "t.amount, CONCAT('', t.paymentId), t.status) " +
            "FROM TransactionEntity t " +
            "LEFT JOIN t.user u " +
            "LEFT JOIN Vehicle v ON v.user = u " +
            "ORDER BY t.timeDate DESC")
    List<TransactionDTO> findAllTransactionsWithDetails();

    @Query("SELECT t FROM TransactionEntity t " +
            "LEFT JOIN FETCH t.user " +
            "WHERE t.transactionId = :id")
    Optional<TransactionEntity> findByIdWithUser(@Param("id") Long id);

    // ✅ SỬA DÒNG NÀY: Đổi t.userId → t.user.id
    @Query("SELECT t FROM TransactionEntity t " +
            "LEFT JOIN FETCH t.user " +
            "WHERE t.user.id = :userId " +  // ← ĐÃ SỬA
            "ORDER BY t.timeDate DESC")
    List<TransactionEntity> findByUserIdWithUser(@Param("userId") Long userId);

    @Query("SELECT new com.evswap.evswapstation.dto.TransactionBatteryDTO(" +
            "t.transactionId, u.userName, u.phone, i.status, " +
            "i.battery.batteryID, t.returnDate) " +
            "FROM TransactionEntity t " +
            "JOIN t.user u " +
            "LEFT JOIN Inventory i ON t.inventoryId = i.inventoryID " +
            "ORDER BY t.transactionId DESC")
    List<TransactionBatteryDTO> findAllTransactionBatteryInfo();

}