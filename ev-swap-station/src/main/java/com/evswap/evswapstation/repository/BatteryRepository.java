package com.evswap.evswapstation.repository;

import com.evswap.evswapstation.entity.Battery;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BatteryRepository extends JpaRepository<Battery, Integer> {
    List<Battery> findByBatteryNameContaining(String keyword);
    long countByStatus(String status);

    List<Battery> findByCapacityAndModelAndStatus(Integer capacity, String model, String status);
    List<Battery> findByCapacity(Integer capacity);
    List<Battery> findByModel(String model);
    List<Battery> findByStatus(String status);
}