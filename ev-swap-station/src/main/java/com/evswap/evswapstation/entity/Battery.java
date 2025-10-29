package com.evswap.evswapstation.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Battery")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Battery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer batteryID;

    private String batteryName;
    private Double price;
    private String status;
    private String detailInformation;
}
