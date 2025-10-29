package com.evswap.evswapstation.repository;

import com.evswap.evswapstation.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {
    List<Vehicle> findByUser_UserID(Integer userId);
    Optional<Vehicle> findByVin(String vin);
}

