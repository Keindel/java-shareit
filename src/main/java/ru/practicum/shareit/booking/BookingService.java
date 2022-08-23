package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.exceptions.BookingNotFoundException;
import ru.practicum.shareit.exceptions.BookingValidationException;
import ru.practicum.shareit.exceptions.ItemNotFoundException;
import ru.practicum.shareit.exceptions.UserNotFoundException;

import java.util.Collection;

public interface BookingService {
    Booking makeBooking(long userId, BookingInputDto bookingInputDto) throws ItemNotFoundException, BookingValidationException, UserNotFoundException;
    Booking updateStatus(long userId, long bookingId, boolean approved) throws BookingNotFoundException, ItemNotFoundException, BookingValidationException, UserNotFoundException;
    Booking getById(long userId, long bookingId) throws ItemNotFoundException, BookingNotFoundException, BookingValidationException, UserNotFoundException;
    Collection<Booking> getAllByBookerId(long bookerId, State state) throws UserNotFoundException, BookingValidationException;
    Collection<Booking> getAllByOwnerId(long ownerId, State state) throws UserNotFoundException, BookingValidationException;
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