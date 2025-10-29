package com.evswap.evswapstation.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Liên kết với User
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "station_id", nullable = false)
    private Station station;

    @ManyToOne
    @JoinColumn(name = "package_id", nullable = false)
    private PackagePlan packagePlan;


    private Double amount;
    private LocalDateTime transactionTime;
    private String status; // SUCCESS, FAILED, PENDING
    private String method; // CREDIT_CARD, MOMO, ZALOPAY
}
