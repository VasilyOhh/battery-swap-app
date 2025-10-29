package com.evswap.evswapstation.controller;

import com.evswap.evswapstation.entity.Station;
import com.evswap.evswapstation.service.StationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stations")
@RequiredArgsConstructor
public class StationController {
    private final StationService stationService;

    @GetMapping("/nearby")
    public ResponseEntity<?> getNearbyStations(
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam(defaultValue = "5") double radiusKm
    ) {
        return ResponseEntity.ok(stationService.findNearbyStations(lat, lng, radiusKm));
    }


    @GetMapping
    public ResponseEntity<List<Station>> getAll() {
        return ResponseEntity.ok(stationService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Station> getById(@PathVariable Integer id) {
        return stationService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Station> create(@RequestBody Station station) {
        return ResponseEntity.ok(stationService.create(station));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Station> update(@PathVariable Integer id, @RequestBody Station station) {
        return ResponseEntity.ok(stationService.update(id, station));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        stationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
