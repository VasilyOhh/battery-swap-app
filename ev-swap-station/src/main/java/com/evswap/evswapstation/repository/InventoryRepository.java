package com.evswap.evswapstation.repository;

import com.evswap.evswapstation.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Integer> {
    List<Inventory> findByStation_StationID(Integer stationId);
    List<Inventory> findByBattery_BatteryID(Integer batteryId);
}