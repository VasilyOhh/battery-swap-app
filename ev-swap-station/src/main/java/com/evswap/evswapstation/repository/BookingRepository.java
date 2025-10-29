package com.evswap.evswapstation.repository;

import com.evswap.evswapstation.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findByUserUserID(Integer userId);
    List<Booking> findByStationStationID(Integer stationId);
    List<Booking> findByVehicleVehicleID(Integer vehicleId);
}