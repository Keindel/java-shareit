package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDtoMapper;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.exceptions.*;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.util.CustomPageable;

import java.time.LocalDateTime;
import java.util.Collection;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    private final BookingDtoMapper bookingDtoMapper;

    @Override
    public Booking makeBooking(long userId, BookingInputDto bookingInputDto)
            throws ItemNotFoundException, BookingValidationException, UserNotFoundException, BookingNotFoundException {
        validateUserPresenceById(userId);
        Item item = itemRepository.findById(bookingInputDto.getItemId()).orElseThrow(ItemNotFoundException::new);
        if (item.getOwner().getId() == userId) {
            throw new BookingNotFoundException("you can't book your own item");
        }
        if (item.getAvailable().equals(false)) {
            throw new BookingValidationException("item is unavailable for booking");
        }
        Booking booking = bookingDtoMapper.mapToBooking(bookingInputDto);
        booking.setBooker(userRepository.findById(userId).orElseThrow(UserNotFoundException::new));
        booking.setItem(item);
        booking.setStatus(Status.WAITING);
        return bookingRepository.save(booking);
    }

    private void validateUserPresenceById(long userId) throws UserNotFoundException {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException();
        }
    }

    @Override
    public Booking updateStatus(long userId, long bookingId, boolean approved)
            throws BookingNotFoundException, ItemNotFoundException, BookingValidationException, UserNotFoundException {
        validateUserPresenceById(userId);
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(BookingNotFoundException::new);
        if (booking.getStatus() == Status.APPROVED) {
            throw new BookingValidationException("approved status cant be changed");
        }
        long itemId = booking.getItem().getId();
        if (itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new)
                .getOwner().getId() != userId) {
            throw new BookingNotFoundException("current user is not an owner");
        }
        if (approved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        return booking;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public Booking getById(long userId, long bookingId)
            throws ItemNotFoundException, BookingNotFoundException, UserNotFoundException {
        validateUserPresenceById(userId);
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(BookingNotFoundException::new);
        long bookerId = booking.getBooker().getId();
        long ownerId = itemRepository.findById(booking.getItem().getId()).orElseThrow(ItemNotFoundException::new).getOwner().getId();
        if (userId != bookerId && userId != ownerId) {
            throw new BookingNotFoundException("only owner and booker have access");
        }
        return booking;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public Collection<Booking> getAllByBookerId(long bookerId, String state, Integer from, Integer size)
            throws UserNotFoundException, UnsupportedStateException {
        validateUserPresenceById(bookerId);

        Pageable page = CustomPageable.of(from, size, Sort.sort(Booking.class).by(Booking::getStart).descending());
        switch (state) {
            case "ALL":
                return bookingRepository.findAllByBookerId(bookerId, page);
            case "PAST":
                return bookingRepository.findAllByBookerIdAndEndIsBefore(bookerId, LocalDateTime.now(), page);
            case "CURRENT":
                LocalDateTime now = LocalDateTime.now();
                return bookingRepository.findAllByBookerIdAndStartIsBeforeAndEndIsAfter(bookerId, now, now, page);
            case "FUTURE":
                return bookingRepository.findAllByBookerIdAndStartIsAfter(bookerId, LocalDateTime.now(), page);
            case "WAITING":
                return bookingRepository.findAllByBookerIdAndStatusEquals(bookerId, Status.WAITING, page);
            case "REJECTED":
                return bookingRepository.findAllByBookerIdAndStatusEquals(bookerId, Status.REJECTED, page);
            default:
                throw new UnsupportedStateException("Unknown state: UNSUPPORTED_STATUS");
        }
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public Collection<Booking> getAllByOwnerId(long ownerId, String state, Integer from, Integer size)
            throws UserNotFoundException, UnsupportedStateException {
        validateUserPresenceById(ownerId);

        Pageable page = CustomPageable.of(from, size, Sort.sort(Booking.class).by(Booking::getStart).descending());
        switch (state) {
            case "ALL":
                return bookingRepository.findAllByItemOwnerId(ownerId, page);
            case "PAST":
                return bookingRepository.findAllByItemOwnerIdAndEndIsBefore(ownerId, LocalDateTime.now(), page);
            case "CURRENT":
                LocalDateTime now = LocalDateTime.now();
                return bookingRepository.findAllByItemOwnerIdAndStartIsBeforeAndEndIsAfter(ownerId, now, now, page);
            case "FUTURE":
                return bookingRepository.findAllByItemOwnerIdAndStartIsAfter(ownerId, LocalDateTime.now(), page);
            case "WAITING":
                return bookingRepository.findAllByItemOwnerIdAndStatusEquals(ownerId, Status.WAITING, page);
            case "REJECTED":
                return bookingRepository.findAllByItemOwnerIdAndStatusEquals(ownerId, Status.REJECTED, page);
            default:
                throw new UnsupportedStateException("Unknown state: UNSUPPORTED_STATUS");
        }
    }
}