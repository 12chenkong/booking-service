package com.example.demo.messaging;

import com.example.demo.events.BookingCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookingEventProducer {

    private final KafkaTemplate<String,Object>kafkaTemplate;

    public void publishBookingEvent(BookingCreatedEvent bookingCreatedEvent){
        try {

            log.info("Publishing booking event with id: {}", bookingCreatedEvent.bookingId());

            kafkaTemplate.send("movie-booking-events", bookingCreatedEvent.bookingId(),bookingCreatedEvent);

        }catch (Exception e){
            log.error("Failed to publish booking event with id: {}, error: {}", bookingCreatedEvent.bookingId(), e.getMessage());
        }

    }
}
