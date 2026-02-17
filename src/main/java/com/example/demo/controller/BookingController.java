package com.example.demo.controller;

import com.example.demo.service.BookingService;
import com.example.demo.dtos.BookingRequest;
import com.example.demo.dtos.BookingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/booking-service")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    // Endpoint to handle seat booking requests
    @PostMapping("/bookSeat")
    public ResponseEntity<?> bookSeat(@RequestBody BookingRequest request) {
        BookingResponse response = bookingService.bookSeats(request);
        return ResponseEntity.ok(response);
    }
}
