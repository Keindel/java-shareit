package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.dto.BookingOutputDto;
import ru.practicum.shareit.booking.dto.BookingDtoMapper;
import ru.practicum.shareit.exceptions.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final BookingDtoMapper bookingDtoMapper;

    @PostMapping
    public BookingOutputDto makeBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                        @Valid @RequestBody BookItemRequestDto bookingInputDto)
            throws BookingValidationException, ItemNotFoundException, UserNotFoundException, BookingNotFoundException {
        return bookingDtoMapper.mapToOutputDto(bookingService.makeBooking(userId,
                bookingInputDto));
    }

    @PatchMapping("/{bookingId}")
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
    public Collection<BookingOutputDto> getAllByBookerId(@RequestHeader("X-Sharer-User-Id") long bookerId,
                                                         @RequestParam(required = false, defaultValue = "ALL") String state,
                                                         @Valid @Min(0) @RequestParam(required = false) Integer from,
                                                         @Valid @Min(1) @RequestParam(required = false) Integer size)
            throws UserNotFoundException, BookingValidationException, UnsupportedStateException {
        return bookingDtoMapper.mapToDtoCollection(bookingService.getAllByBookerId(bookerId, state, from, size));
    }

    @GetMapping("/owner")
    public Collection<BookingOutputDto> getAllByOwnerId(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                                        @RequestParam(required = false, defaultValue = "ALL") String state,
                                                        @Valid @Min(0) @RequestParam(required = false) Integer from,
                                                        @Valid @Min(1) @RequestParam(required = false) Integer size)
            throws UserNotFoundException, BookingValidationException, UnsupportedStateException {
        return bookingDtoMapper.mapToDtoCollection(bookingService.getAllByOwnerId(ownerId, state, from, size));
    }
}