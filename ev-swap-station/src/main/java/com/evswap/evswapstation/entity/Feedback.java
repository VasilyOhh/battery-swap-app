package com.evswap.evswapstation.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Feedback")
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Liên kết với User
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Liên kết với Station (nếu feedback về trạm)
    @ManyToOne
    @JoinColumn(name = "station_id")
    private Station station;

    private String content;
    private int rating; // 1-5 stars
}
