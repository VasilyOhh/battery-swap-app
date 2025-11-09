package com.evswap.evswapstation.service;

import com.evswap.evswapstation.dto.TransactionBatteryDTO;
import com.evswap.evswapstation.dto.TransactionDTO;
import com.evswap.evswapstation.entity.TransactionEntity;
import com.evswap.evswapstation.entity.User;
import com.evswap.evswapstation.repository.TransactionRepository;
import com.evswap.evswapstation.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<TransactionDTO> getAllTransactions() {
        return transactionRepository.findAllTransactionsWithDetails();
    }

    @Transactional(readOnly = true)
    public TransactionDTO getTransactionById(Long id) {
        return transactionRepository.findByIdWithUser(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public List<TransactionDTO> getTransactionsByStatus(String status) {
        return transactionRepository.findByStatus(status)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TransactionDTO> getTransactionsByUserId(Long userId) {
        return transactionRepository.findByUserIdWithUser(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Tạo giao dịch mới
     */
    @Transactional
    public TransactionDTO createTransaction(TransactionDTO dto) {
        TransactionEntity transaction = new TransactionEntity();

        // Set User (bắt buộc)
        if (dto.getUserId() == null) {
            throw new IllegalArgumentException("User ID is required");
        }
        User user = userRepository.findById(dto.getUserId().intValue())  // ✅ ĐÃ SỬA
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + dto.getUserId()));
        transaction.setUser(user);

        // Set các field khác
        transaction.setStationId(dto.getStationId());
        transaction.setPackageId(dto.getPackageId());
        transaction.setAmount(dto.getAmount());
        transaction.setStatus(dto.getStatus() != null ? dto.getStatus() : "PENDING");
        transaction.setTimeDate(dto.getTimeDate() != null ? dto.getTimeDate() : LocalDateTime.now());
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setRecord(dto.getRecord());
        transaction.setPaymentId(dto.getPaymentId() != null ? Long.parseLong(dto.getPaymentId()) : null);
        transaction.setPayPalTransactionId(dto.getPayPalTransactionId());

        TransactionEntity saved = transactionRepository.save(transaction);
        return convertToDTO(saved);
    }

    /**
     * Cập nhật giao dịch
     */
    @Transactional
    public TransactionDTO updateTransaction(Long id, TransactionDTO dto) {
        return transactionRepository.findById(id)
                .map(transaction -> {
                    // Update User nếu có
                    if (dto.getUserId() != null) {
                        User user = userRepository.findById(dto.getUserId().intValue())  // ✅ SỬA DÒNG NÀY
                                .orElseThrow(() -> new RuntimeException("User not found with ID: " + dto.getUserId()));
                        transaction.setUser(user);
                    }

                    // Update các field khác nếu có giá trị mới
                    if (dto.getStationId() != null) {
                        transaction.setStationId(dto.getStationId());
                    }
                    if (dto.getPackageId() != null) {
                        transaction.setPackageId(dto.getPackageId());
                    }
                    if (dto.getAmount() != null) {
                        transaction.setAmount(dto.getAmount());
                    }
                    if (dto.getStatus() != null) {
                        transaction.setStatus(dto.getStatus());
                    }
                    if (dto.getRecord() != null) {
                        transaction.setRecord(dto.getRecord());
                    }
                    if (dto.getPaymentId() != null) {
                        transaction.setPaymentId(Long.parseLong(dto.getPaymentId()));
                    }
                    if (dto.getPayPalTransactionId() != null) {
                        transaction.setPayPalTransactionId(dto.getPayPalTransactionId());
                    }
                    if (dto.getTimeDate() != null) {
                        transaction.setTimeDate(dto.getTimeDate());
                    }

                    TransactionEntity updated = transactionRepository.save(transaction);
                    return convertToDTO(updated);
                })
                .orElse(null);
    }

    /**
     * Xóa giao dịch
     */
    @Transactional
    public boolean deleteTransaction(Long id) {
        if (transactionRepository.existsById(id)) {
            transactionRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Convert Entity sang DTO
     */
    private TransactionDTO convertToDTO(TransactionEntity transaction) {
        TransactionDTO dto = new TransactionDTO();

        // Set basic fields
        dto.setTransactionId(transaction.getTransactionId());
        dto.setTimeDate(transaction.getTimeDate());
        dto.setAmount(transaction.getAmount());
        dto.setStatus(transaction.getStatus());
        dto.setPaymentId(transaction.getPaymentId() != null ? transaction.getPaymentId().toString() : null);
        dto.setPayPalTransactionId(transaction.getPayPalTransactionId());
        dto.setRecord(transaction.getRecord());
        dto.setStationId(transaction.getStationId());
        dto.setPackageId(transaction.getPackageId());

        // Set User info
        if (transaction.getUser() != null) {
            dto.setUserId(transaction.getUser().getUserID().longValue());  // ✅ SỬA DÒNG NÀY
            dto.setCustomerName(transaction.getUser().getFullName());
            dto.setCustomerEmail(transaction.getUser().getEmail());
        }

        // VIN sẽ null nếu không có Vehicle relationship
        dto.setVin(null);

        return dto;
    }

    @Transactional(readOnly = true)
    public List<TransactionBatteryDTO> getAllTransactionBatteryInfo() {
        return transactionRepository.findAllTransactionBatteryInfo();
    }
}