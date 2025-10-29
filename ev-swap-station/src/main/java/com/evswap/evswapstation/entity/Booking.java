package com.evswap.evswapstation.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Table(name = "Booking")
@Data // <-- Thêm dòng này để Lombok tự sinh getter/setter/toString/equals/hashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Quan hệ với User
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Quan hệ với Station
    @ManyToOne
    @JoinColumn(name = "station_id", nullable = false)
    private Station station;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    private LocalDateTime timeDate;


    private LocalDateTime bookingTime;
    private String status; // PENDING, CONFIRMED, CANCELLED
}
