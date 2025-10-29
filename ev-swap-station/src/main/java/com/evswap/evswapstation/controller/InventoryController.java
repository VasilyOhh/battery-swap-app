package com.evswap.evswapstation.controller;

import com.evswap.evswapstation.entity.Inventory;
import com.evswap.evswapstation.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventories")
public class InventoryController {
    private final InventoryService service;

    public InventoryController(InventoryService service) {
        this.service = service;
    }

    @Operation(summary = "Lấy danh sách tất cả Inventories")
    @GetMapping
    public List<Inventory> getAll() {
        return service.findAll();
    }

    @Operation(summary = "Tìm Inventory theo ID")
    @GetMapping("/{id}")
    public Inventory getById(@PathVariable Integer id) {
        return service.findById(id);
    }

    @Operation(summary = "Thêm mới Inventory")
    @PostMapping
    public Inventory create(@RequestBody Inventory inventory) {
        return service.save(inventory);
    }

    @Operation(summary = "Cập nhật Inventory")
    @PutMapping("/{id}")
    public Inventory update(@PathVariable Integer id, @RequestBody Inventory inventory) {
        inventory.setInventoryID(id);
        return service.save(inventory);
    }

    @Operation(summary = "Xóa Inventory theo ID")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }
}
