package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.dto.BookingOutputDto;
import ru.practicum.shareit.booking.dto.BookingDtoMapper;
import ru.practicum.shareit.exceptions.BookingNotFoundException;
import ru.practicum.shareit.exceptions.BookingValidationException;
import ru.practicum.shareit.exceptions.ItemNotFoundException;
import ru.practicum.shareit.exceptions.UserNotFoundException;

import javax.validation.Valid;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final BookingDtoMapper bookingDtoMapper;

    @PostMapping
    public Booking makeBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                        @Valid @RequestBody BookingInputDto bookingInputDto)
            throws BookingValidationException, ItemNotFoundException, UserNotFoundException {
        Booking booking = bookingService.makeBooking(userId,
                bookingInputDto);
        return booking;
    }

    @PatchMapping("/{bookingId}?approved={approved}")
    public BookingOutputDto updateStatus(@RequestHeader("X-Sharer-User-Id") long userId,
                                         @PathVariable long bookingId,
                                         @RequestParam boolean approved)
            throws BookingNotFoundException, BookingValidationException, ItemNotFoundException, UserNotFoundException {
        return bookingDtoMapper.mapToOutputDto(bookingService.updateStatus(userId, bookingId, approved));
    }

    @GetMapping("/{bookingId}")
    public BookingOutputDto getById(@RequestHeader("X-Sharer-User-Id") long userId,
                                    @PathVariable long bookingId)
            throws BookingNotFoundException, BookingValidationException, ItemNotFoundException, UserNotFoundException {
        return bookingDtoMapper.mapToOutputDto(bookingService.getById(userId, bookingId));
    }

    @GetMapping
    // TODO State - String?
    public Collection<BookingOutputDto> getAllByBookerId(@RequestHeader("X-Sharer-User-Id") long bookerId,
                                                         @RequestParam(required = false, defaultValue = "All") State state)
            throws UserNotFoundException, BookingValidationException {
        return bookingDtoMapper.mapToDtoCollection(bookingService.getAllByBookerId(bookerId, state));
    }

    @GetMapping("/owner")
    public Collection<BookingOutputDto> getAllByOwnerId(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                                        @RequestParam(required = false, defaultValue = "All") State state)
            throws UserNotFoundException, BookingValidationException {
        return bookingDtoMapper.mapToDtoCollection(bookingService.getAllByOwnerId(ownerId, state));
    }
}