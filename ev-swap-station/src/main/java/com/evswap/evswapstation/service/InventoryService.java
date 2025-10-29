package com.evswap.evswapstation.service;

import com.evswap.evswapstation.entity.Inventory;
import com.evswap.evswapstation.repository.InventoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {
    private final InventoryRepository repo;

    public InventoryService(InventoryRepository repo) {
        this.repo = repo;
    }

    public List<Inventory> findAll() {
        return repo.findAll();
    }

    public Inventory findById(Integer id) {
        return repo.findById(id).orElse(null);
    }

    public Inventory save(Inventory inventory) {
        return repo.save(inventory);
    }

    public void delete(Integer id) {
        repo.deleteById(id);
    }
}
