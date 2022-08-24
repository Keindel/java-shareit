package ru.practicum.shareit.booking.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.Booking;

import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class BookingDtoMapper {
//    static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public BookingOutputDto mapToOutputDto(Booking booking) {
        return BookingOutputDto.builder()
                .id(booking.getId())
//                .start(formatter.format(booking.getStart()))
//                .end(formatter.format(booking.getEnd()))
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(booking.getItem())
                .booker(booking.getBooker())
                .status(booking.getStatus())
                .build();
    }

    public BookingBookerIdDto mapToBookerIdDto(Booking booking) {
        if (booking == null) {
            return null;
        }
        return new BookingBookerIdDto(booking.getId(), booking.getBooker().getId());
    }

    public Booking mapToBooking(BookingInputDto bookingInputDto) {
        Booking booking = new Booking();
//        booking.setId(bookingInputDto.getId());
//        booking.setStart(LocalDateTime.parse(bookingDto.getStart(), formatter));
//        booking.setEnd(LocalDateTime.parse(bookingDto.getEnd(), formatter));
        booking.setStart(bookingInputDto.getStart());
        booking.setEnd(bookingInputDto.getEnd());
        return booking;
    }

    public Collection<BookingOutputDto> mapToDtoCollection(Collection<Booking> bookings){
        return bookings.stream().map(this::mapToOutputDto).collect(Collectors.toList());
    }
}
