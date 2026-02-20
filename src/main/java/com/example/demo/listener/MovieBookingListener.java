package com.example.demo.listener;

import com.example.demo.events.SeatReservedEvent;
import com.example.demo.repository.BookingRepository;
import com.example.demo.service.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class MovieBookingListener {

    private final BookingService bookingService;


    @KafkaListener(topics = "seat-reserved-topc", groupId = "movie-booking-group")
    public void consumeSeatReservedEvent(SeatReservedEvent event) {


        if(event.reserved()){
            log.info("booking process completed for bookingId{}", event.bookingId());
        }else {
            log.warn("booking process failed for bookingId{}", event.bookingId());
            bookingService.handleBookingOnSeatReservationFailure(event.bookingId());
        }

    }
}
