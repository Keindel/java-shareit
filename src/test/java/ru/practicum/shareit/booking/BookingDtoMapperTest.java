package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDtoMapper;
import ru.practicum.shareit.booking.dto.BookingOutputDto;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class BookingDtoMapperTest {

    private Booking booking = new Booking(1L, LocalDateTime.now(), LocalDateTime.now(), new Item(), new User(), null);
    private Booking otherBooking = new Booking(7L, LocalDateTime.now(), LocalDateTime.now(), new Item(), new User(), null);

    private BookingDtoMapper bookingDtoMapper = new BookingDtoMapper();

    @Test
    public void checkMapToDtoCollection() {
        Collection<Booking> bookings = List.of(booking, otherBooking);
        Collection<BookingOutputDto> bookingOutputDtos = bookingDtoMapper.mapToDtoCollection(bookings);

        assertThat(bookingOutputDtos, hasSize(2));
        assertThat(bookingOutputDtos, hasItem(BookingOutputDto.builder().id(1L).build()));
        assertThat(bookingOutputDtos, hasItem(BookingOutputDto.builder().id(7L).build()));
    }
}
