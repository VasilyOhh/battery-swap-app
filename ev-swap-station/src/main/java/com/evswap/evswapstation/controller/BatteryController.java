package com.evswap.evswapstation.controller;

import com.evswap.evswapstation.entity.Battery;
import com.evswap.evswapstation.service.BatteryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/batteries")
@RequiredArgsConstructor
public class BatteryController {
    private final BatteryService service;

    @GetMapping public List<Battery> getAll() { return service.getAll(); }
    @GetMapping("/{id}") public ResponseEntity<Battery> getById(@PathVariable Integer id) {
        return service.getById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @PostMapping public Battery create(@RequestBody Battery b) { return service.create(b); }
    @PutMapping("/{id}") public Battery update(@PathVariable Integer id, @RequestBody Battery b) { return service.update(id, b); }
    @DeleteMapping("/{id}") public void delete(@PathVariable Integer id) { service.delete(id); }
}
