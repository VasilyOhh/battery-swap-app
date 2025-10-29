package com.evswap.evswapstation.controller;

import com.evswap.evswapstation.entity.Booking;
import com.evswap.evswapstation.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @GetMapping
    public List<Booking> getAll() {
        return bookingService.getAll();
    }

    @GetMapping("/{id}")
    public Booking getById(@PathVariable Integer id) {
        return bookingService.getById(id).orElseThrow(() -> new RuntimeException("Booking not found"));
    }

    @PostMapping
    public Booking create(@RequestBody Booking booking) {
        return bookingService.create(booking);
    }

    @PutMapping("/{id}")
    public Booking update(@PathVariable Integer id, @RequestBody Booking booking) {
        return bookingService.update(id, booking);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        bookingService.delete(id);
    }
}
