package com.evswap.evswapstation.service;

import com.evswap.evswapstation.entity.Booking;
import com.evswap.evswapstation.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;

    public List<Booking> getAll() {
        return bookingRepository.findAll();
    }

    public Optional<Booking> getById(Integer id) {
        return bookingRepository.findById(id);
    }

    public Booking create(Booking booking) {
        return bookingRepository.save(booking);
    }

    public Booking update(Integer id, Booking booking) {
        return bookingRepository.findById(id)
                .map(b -> {
                    b.setStation(booking.getStation());
                    b.setUser(booking.getUser());
                    b.setVehicle(booking.getVehicle());
                    b.setTimeDate(booking.getTimeDate());
                    return bookingRepository.save(b);
                }).orElseThrow(() -> new RuntimeException("Booking not found"));
    }

    public void delete(Integer id) {
        bookingRepository.deleteById(id);
    }
}
