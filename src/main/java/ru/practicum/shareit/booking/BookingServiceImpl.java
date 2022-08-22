package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.BookingNotFoundException;
import ru.practicum.shareit.exceptions.BookingValidationException;
import ru.practicum.shareit.exceptions.ItemNotFoundException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
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

    @Override
    public Booking makeBooking(long userId, Booking booking)
            throws ItemNotFoundException, BookingValidationException, UserNotFoundException {
        validateUserPresenceById(userId);
        if (itemRepository.findById(booking.getItem().getId()).orElseThrow(ItemNotFoundException::new)
                .getOwner().getId() == userId) {
            throw new BookingValidationException("you can't book your own item");
        }
        booking.setBooker(userRepository.findById(userId).orElseThrow(UserNotFoundException::new));
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
        long itemId = booking.getItem().getId();
        if (itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new)
                .getOwner().getId() != userId) {
            throw new BookingValidationException("current user is not an owner");
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
            throw new BookingValidationException("only owner and booker have access");
        }
        return bookingRepository.findById(bookingId).orElseThrow(BookingNotFoundException::new);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public Collection<Booking> getAllByBookerId(long bookerId, State state)
            throws UserNotFoundException, BookingValidationException {
        validateUserPresenceById(bookerId);
        Sort sort = Sort.sort(Booking.class).by(Booking::getStart).descending();
        switch (state) {
            case ALL:
                return bookingRepository.findAllByBookerId(bookerId, sort);
            case PAST:
                return bookingRepository.findAllByBookerIdAndEndIsBefore(bookerId, LocalDateTime.now(), sort);
            case CURRENT:
                LocalDateTime now = LocalDateTime.now();
                return bookingRepository.findAllByBookerIdAndStartIsBeforeAndEndIsAfter(bookerId, now, now, sort);
            case FUTURE:
                return bookingRepository.findAllByBookerIdAndStartIsAfter(bookerId, LocalDateTime.now(), sort);
            case WAITING:
                //TODO Status as String?
                return bookingRepository.findAllByBookerIdAndStatusEquals(bookerId, Status.WAITING, sort);
            case REJECTED:
                //TODO Status as String?
                return bookingRepository.findAllByBookerIdAndStatusEquals(bookerId, Status.REJECTED, sort);
            default:
                throw new BookingValidationException("invalid booking state request");
        }
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public Collection<Booking> getAllByOwnerId(long ownerId, State state) throws UserNotFoundException, BookingValidationException {
        validateUserPresenceById(ownerId);
//        User owner = userRepository.findById(ownerId).orElseThrow(UserNotFoundException::new);
//        List<Item> ownerItems = owner.getItemsForSharing();
//        if (ownerItems.isEmpty()) {
//            return List.of();
//        }
        Sort sort = Sort.sort(Booking.class).by(Booking::getStart).descending();
        switch (state) {
            case ALL:
                return bookingRepository.findAllByItemOwnerId(ownerId, sort);
            case PAST:
                return bookingRepository.findAllByItemOwnerIdAndEndIsBefore(ownerId, LocalDateTime.now(), sort);
            case CURRENT:
                LocalDateTime now = LocalDateTime.now();
                return bookingRepository.findAllByItemOwnerIdAndStartIsBeforeAndEndIsAfter(ownerId, now, now, sort);
            case FUTURE:
                return bookingRepository.findAllByItemOwnerIdAndStartIsAfter(ownerId, LocalDateTime.now(), sort);
            case WAITING:
                //TODO Status as String?
                return bookingRepository.findAllByItemOwnerIdAndStatusEquals(ownerId, Status.WAITING, sort);
            case REJECTED:
                //TODO Status as String?
                return bookingRepository.findAllByItemOwnerIdAndStatusEquals(ownerId, Status.REJECTED, sort);
            default:
                throw new BookingValidationException("invalid booking state request");
        }
//        return ownerItems.stream().map(Item::getBookings)
//                .flatMap(Collection::stream)
//                .collect(Collectors.toList());
    }
}
/*Добавление нового запроса на бронирование. Запрос может быть создан любым пользователем,
 *  а затем подтверждён владельцем вещи. Эндпоинт — POST /bookings. После создания запрос находится
 *  в статусе WAITING — «ожидает подтверждения».

 * Подтверждение или отклонение запроса на бронирование. Может быть выполнено только владельцем вещи.
 *  Затем статус бронирования становится либо APPROVED, либо REJECTED.
 *  Эндпоинт — PATCH /bookings/{bookingId}?approved={approved},
 *  параметр approved может принимать значения true или false.

 * Получение данных о конкретном бронировании (включая его статус).
 *  Может быть выполнено либо автором бронирования, либо владельцем вещи,
 *  к которой относится бронирование. Эндпоинт — GET /bookings/{bookingId}.

 * Получение списка всех бронирований текущего пользователя.
 *  Эндпоинт — GET /bookings?state={state}.
 *  Параметр state необязательный и по умолчанию равен ALL (англ. «все»).
 *  Также он может принимать значения CURRENT (англ. «текущие»), **PAST** (англ. «завершённые»),
 *  FUTURE (англ. «будущие»), WAITING (англ. «ожидающие подтверждения»),
 *  REJECTED (англ. «отклонённые»).
 *  Бронирования должны возвращаться отсортированными по дате от более новых к более старым.

 * Получение списка бронирований для всех вещей текущего пользователя.
 *  Эндпоинт — GET /bookings/owner?state={state}.
 *  Этот запрос имеет смысл для владельца хотя бы одной вещи.
 *  Работа параметра state аналогична его работе в предыдущем сценарии.*/