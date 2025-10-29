package com.evswap.evswapstation.service;

import com.evswap.evswapstation.entity.Battery;
import com.evswap.evswapstation.repository.BatteryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BatteryService {
    private final BatteryRepository repo;

    public List<Battery> getAll() { return repo.findAll(); }
    public Optional<Battery> getById(Integer id) { return repo.findById(id); }
    public Battery create(Battery b) { return repo.save(b); }
    public Battery update(Integer id, Battery b) {
        return repo.findById(id).map(x -> {
            x.setBatteryName(b.getBatteryName());
            x.setPrice(b.getPrice());
            x.setStatus(b.getStatus());
            x.setDetailInformation(b.getDetailInformation());
            return repo.save(x);
        }).orElseThrow(() -> new RuntimeException("Battery not found"));
    }
    public void delete(Integer id) { repo.deleteById(id); }
}
