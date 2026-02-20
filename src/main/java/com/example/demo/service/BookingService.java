package com.example.demo.service;

import com.example.demo.domain.Booking;
import com.example.demo.events.BookingCreatedEvent;
import com.example.demo.messaging.BookingEventProducer;
import com.example.demo.repository.BookingRepository;
import com.example.demo.utils.mapper.BookingRequestToEntityMapper;
import com.example.demo.utils.mapper.EntityToBookingResponseMapper;
import com.example.demo.dtos.BookingRequest;
import com.example.demo.dtos.BookingResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService {

    private final BookingRepository bookingRepository;
    private final BookingEventProducer bookingEventProducer;

    public BookingResponse bookSeats(BookingRequest request) {

        log.info("Booking seats for user {} for show {}", request.userId(), request.showId());

        // Map request -> entity
        var reservationEntity = BookingRequestToEntityMapper.map(request);

        // Persist and map to response
        var savedReservation = bookingRepository.save(reservationEntity);

        // Publish booking created event
        var bookingCreatedEvent = this.buildBookingCreateEvents(savedReservation);
        bookingEventProducer.publishBookingEvent(bookingCreatedEvent);

        var response = EntityToBookingResponseMapper.map(savedReservation);

        log.info("Seats confirmed with reservation id {}", response.reservationId());
        return response;
    }

    private BookingCreatedEvent buildBookingCreateEvents(Booking savedReservation){
        return new BookingCreatedEvent(savedReservation.getBookingCode()
                ,savedReservation.getUserId(),savedReservation.getShowId(),
                savedReservation.getSeatIds(),savedReservation.getAmount());
    }

    public void handleBookingOnSeatReservationFailure(String bookingId){
        var bookingDetail=bookingRepository.findByBookingCode(bookingId)
                .orElseThrow(()->new RuntimeException("Booking not found for bookingId"+bookingId));

        bookingDetail.setStatus("FAILED");
        bookingRepository.save(bookingDetail);
        log.info("Booking with id {} marked as FAILED due to seat reservation failure", bookingId);
    }


}
