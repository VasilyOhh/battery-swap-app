package com.evswap.evswapstation.controller;

import com.evswap.evswapstation.dto.TransactionBatteryDTO;
import com.evswap.evswapstation.dto.TransactionDTO;
import com.evswap.evswapstation.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TransactionController {

    private final TransactionService transactionService;

    /**
     * GET /api/transactions
     * Lấy tất cả giao dịch với thông tin: Transaction ID, Date & Time, Customer, VIN, Amount, Payment
     */
    @GetMapping
    public ResponseEntity<List<TransactionDTO>> getAllTransactions() {
        List<TransactionDTO> transactions = transactionService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }

    /**
     * GET /api/transactions/{id}
     * Lấy chi tiết một giao dịch theo ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<TransactionDTO> getTransactionById(@PathVariable Long id) {
        TransactionDTO transaction = transactionService.getTransactionById(id);
        if (transaction != null) {
            return ResponseEntity.ok(transaction);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * GET /api/transactions/status/{status}
     * Lấy giao dịch theo trạng thái (PENDING, COMPLETED, FAILED, CANCELLED)
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByStatus(@PathVariable String status) {
        List<TransactionDTO> transactions = transactionService.getTransactionsByStatus(status);
        return ResponseEntity.ok(transactions);
    }

    /**
     * GET /api/transactions/user/{userId}
     * Lấy tất cả giao dịch của một khách hàng
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByUserId(@PathVariable Long userId) {
        List<TransactionDTO> transactions = transactionService.getTransactionsByUserId(userId);
        return ResponseEntity.ok(transactions);
    }

    /**
     * POST /api/transactions
     * Tạo giao dịch mới
     * Body example:
     * {
     *   "userId": 1,
     *   "amount": 150.50,
     *   "stationId": 5,
     *   "packageId": 3,
     *   "status": "PENDING",
     *   "record": "Battery swap transaction"
     * }
     */
    @PostMapping
    public ResponseEntity<?> createTransaction(@RequestBody TransactionDTO transactionDTO) {
        try {
            TransactionDTO createdTransaction = transactionService.createTransaction(transactionDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTransaction);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating transaction: " + e.getMessage());
        }
    }

    /**
     * PUT /api/transactions/{id}
     * Cập nhật giao dịch theo ID
     * Body example:
     * {
     *   "status": "COMPLETED",
     *   "payPalTransactionId": "PAYID-123456",
     *   "amount": 175.00
     * }
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTransaction(
            @PathVariable Long id,
            @RequestBody TransactionDTO transactionDTO) {
        try {
            TransactionDTO updatedTransaction = transactionService.updateTransaction(id, transactionDTO);
            if (updatedTransaction != null) {
                return ResponseEntity.ok(updatedTransaction);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Transaction not found with ID: " + id);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error updating transaction: " + e.getMessage());
        }
    }

    /**
     * DELETE /api/transactions/{id}
     * Xóa giao dịch theo ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTransaction(@PathVariable Long id) {
        try {
            boolean deleted = transactionService.deleteTransaction(id);
            if (deleted) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Transaction not found with ID: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting transaction: " + e.getMessage());
        }
    }

    /**
     * PATCH /api/transactions/{id}/status
     * Cập nhật chỉ status của giao dịch
     * Body example: { "status": "COMPLETED" }
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateTransactionStatus(
            @PathVariable Long id,
            @RequestBody TransactionDTO statusUpdate) {
        try {
            if (statusUpdate.getStatus() == null) {
                return ResponseEntity.badRequest().body("Status is required");
            }

            TransactionDTO transaction = new TransactionDTO();
            transaction.setStatus(statusUpdate.getStatus());

            TransactionDTO updated = transactionService.updateTransaction(id, transaction);
            if (updated != null) {
                return ResponseEntity.ok(updated);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Transaction not found with ID: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error updating status: " + e.getMessage());
        }
    }

    @GetMapping("/battery-info")
    public ResponseEntity<List<TransactionBatteryDTO>> getTransactionBatteryInfo() {
        try {
            List<TransactionBatteryDTO> result = transactionService.getAllTransactionBatteryInfo();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}