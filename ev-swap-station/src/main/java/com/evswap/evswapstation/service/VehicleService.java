package com.evswap.evswapstation.service;

import com.evswap.evswapstation.entity.Vehicle;
import com.evswap.evswapstation.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VehicleService {
    private final VehicleRepository vehicleRepository;

    public List<Vehicle> getAll() {
        return vehicleRepository.findAll();
    }

    public Optional<Vehicle> getById(Integer id) {
        return vehicleRepository.findById(id);
    }

    public Vehicle create(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    public Vehicle update(Integer id, Vehicle vehicle) {
        return vehicleRepository.findById(id)
                .map(v -> {
                    v.setVin(vehicle.getVin());
                    v.setVehicleModel(vehicle.getVehicleModel());
                    v.setBatteryType(vehicle.getBatteryType());
                    v.setRegisterInformation(vehicle.getRegisterInformation());
                    return vehicleRepository.save(v);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle not found"));
    }


    public void delete(Integer id) {
        vehicleRepository.deleteById(id);
    }

    public Vehicle save(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }
}
