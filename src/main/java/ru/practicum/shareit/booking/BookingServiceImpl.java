package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
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
//            throw new BookingValidationException("you can't book your own item");
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
//            throw new BookingValidationException("current user is not an owner");
            throw new BookingNotFoundException("current user is not an owner");
        }
//        if (approved == null) {
//            throw new BookingValidationException();
//        }
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
            throws ItemNotFoundException, BookingNotFoundException, BookingValidationException, UserNotFoundException {
        validateUserPresenceById(userId);
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(BookingNotFoundException::new);
        long bookerId = booking.getBooker().getId();
        long ownerId = itemRepository.findById(booking.getItem().getId()).orElseThrow(ItemNotFoundException::new).getOwner().getId();
        if (userId != bookerId && userId != ownerId) {
//            throw new BookingValidationException("only owner and booker have access");
            throw new BookingNotFoundException("only owner and booker have access");
        }
        return booking;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public Collection<Booking> getAllByBookerId(long bookerId, String state)
            throws UserNotFoundException, UnsupportedStateException {
        validateUserPresenceById(bookerId);
        Sort sort = Sort.sort(Booking.class).by(Booking::getStart).descending();
        switch (state) {
            case "ALL":
                return bookingRepository.findAllByBookerId(bookerId, sort);
            case "PAST":
                return bookingRepository.findAllByBookerIdAndEndIsBefore(bookerId, LocalDateTime.now(), sort);
            case "CURRENT":
                LocalDateTime now = LocalDateTime.now();
                return bookingRepository.findAllByBookerIdAndStartIsBeforeAndEndIsAfter(bookerId, now, now, sort);
            case "FUTURE":
                return bookingRepository.findAllByBookerIdAndStartIsAfter(bookerId, LocalDateTime.now(), sort);
            case "WAITING":
                return bookingRepository.findAllByBookerIdAndStatusEquals(bookerId, Status.WAITING, sort);
            case "REJECTED":
                return bookingRepository.findAllByBookerIdAndStatusEquals(bookerId, Status.REJECTED, sort);
            default:
                throw new UnsupportedStateException("Unknown state: UNSUPPORTED_STATUS");
        }
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public Collection<Booking> getAllByOwnerId(long ownerId, String state)
            throws UserNotFoundException, UnsupportedStateException {
        validateUserPresenceById(ownerId);
//        User owner = userRepository.findById(ownerId).orElseThrow(UserNotFoundException::new);
//        List<Item> ownerItems = owner.getItemsForSharing();
//        if (ownerItems.isEmpty()) {
//            return List.of();
//        }
        Sort sort = Sort.sort(Booking.class).by(Booking::getStart).descending();
        switch (state) {
            case "ALL":
                return bookingRepository.findAllByItemOwnerId(ownerId, sort);
            case "PAST":
                return bookingRepository.findAllByItemOwnerIdAndEndIsBefore(ownerId, LocalDateTime.now(), sort);
            case "CURRENT":
                LocalDateTime now = LocalDateTime.now();
                return bookingRepository.findAllByItemOwnerIdAndStartIsBeforeAndEndIsAfter(ownerId, now, now, sort);
            case "FUTURE":
                return bookingRepository.findAllByItemOwnerIdAndStartIsAfter(ownerId, LocalDateTime.now(), sort);
            case "WAITING":
                return bookingRepository.findAllByItemOwnerIdAndStatusEquals(ownerId, Status.WAITING, sort);
            case "REJECTED":
                return bookingRepository.findAllByItemOwnerIdAndStatusEquals(ownerId, Status.REJECTED, sort);
            default:
                throw new UnsupportedStateException("Unknown state: UNSUPPORTED_STATUS");
        }
    }
}