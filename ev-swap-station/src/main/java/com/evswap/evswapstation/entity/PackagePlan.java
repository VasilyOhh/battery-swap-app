package com.evswap.evswapstation.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "PackagePlans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PackagePlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String planName;
    private Double price;
    private String duration; // "1 month", "6 months", "1 year"
    private String description;
}
