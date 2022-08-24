package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.exceptions.*;

import java.util.Collection;

public interface BookingService {
    Booking makeBooking(long userId, BookingInputDto bookingInputDto) throws ItemNotFoundException, BookingValidationException, UserNotFoundException, BookingNotFoundException;

    Booking updateStatus(long userId, long bookingId, boolean approved) throws BookingNotFoundException, ItemNotFoundException, BookingValidationException, UserNotFoundException;

    Booking getById(long userId, long bookingId) throws ItemNotFoundException, BookingNotFoundException, BookingValidationException, UserNotFoundException;

    Collection<Booking> getAllByBookerId(long bookerId, String stringState) throws UserNotFoundException, BookingValidationException, UnsupportedStateException;

    Collection<Booking> getAllByOwnerId(long ownerId, String stringState) throws UserNotFoundException, BookingValidationException, UnsupportedStateException;
}