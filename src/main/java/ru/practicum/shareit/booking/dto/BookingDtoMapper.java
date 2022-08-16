package ru.practicum.shareit.booking.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.Booking;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class BookingDtoMapper {
    static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");

    public BookingDto mapToDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(formatter.format(booking.getStart()))
                .end(formatter.format(booking.getEnd()))
                .item(booking.getItem())
                .booker(booking.getBooker())
                .status(booking.getStatus())
                .build();
    }

    public Booking mapToBooking(BookingDto bookingDto) {
        Booking booking = new Booking();
        booking.setId(bookingDto.getId());
        booking.setStart(LocalDateTime.parse(bookingDto.getStart(), formatter));
        booking.setEnd(LocalDateTime.parse(bookingDto.getEnd(), formatter));
        booking.setItem(bookingDto.getItem());
        booking.setStatus(bookingDto.getStatus());
        return booking;
    }

    public Collection<BookingDto> mapToDtoCollection(Collection<Booking> bookings){
        return bookings.stream().map(this::mapToDto).collect(Collectors.toList());
    }
}
