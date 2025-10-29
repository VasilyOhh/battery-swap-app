package com.evswap.evswapstation.entity;

import com.evswap.evswapstation.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

@Entity
@Table(name = "Users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userID;

    @Column(nullable = false, unique = true, length = 50)
    private String userName;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(name = "FullName")
    private String fullName;
    private String phone;

    @Convert(converter = com.evswap.evswapstation.util.RoleAttributeConverter.class)
    private Role role;

    private String address;

    // Quan hệ: 1 User có nhiều Vehicles
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Vehicle> vehicles;
}
