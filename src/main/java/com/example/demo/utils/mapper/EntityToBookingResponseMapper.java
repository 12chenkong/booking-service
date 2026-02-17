package com.example.demo.utils.mapper;

import com.example.demo.domain.Booking;
import com.example.demo.dtos.BookingResponse;

public class EntityToBookingResponseMapper {

    public static BookingResponse map(Booking booking) {
        return new BookingResponse(booking.getBookingCode(),
                booking.getStatus());
    }
}