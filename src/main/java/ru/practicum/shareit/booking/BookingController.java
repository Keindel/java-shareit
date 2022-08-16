package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoMapper;
import ru.practicum.shareit.exceptions.BookingNotFoundException;
import ru.practicum.shareit.exceptions.BookingValidationException;
import ru.practicum.shareit.exceptions.ItemNotFoundException;
import ru.practicum.shareit.exceptions.UserNotFoundException;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final BookingDtoMapper bookingDtoMapper;

    @PostMapping
    public BookingDto makeBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                  @RequestBody BookingDto bookingDto) throws BookingValidationException, ItemNotFoundException, UserNotFoundException {
        return bookingDtoMapper.mapToDto(bookingService.makeBooking(userId,
                bookingDtoMapper.mapToBooking(bookingDto)));
    }

    @PatchMapping("/{bookingId}?approved={approved}")
    public BookingDto updateStatus(@RequestHeader("X-Sharer-User-Id") long userId,
                                   @PathVariable long bookingId,
                                   @RequestParam boolean approved) throws BookingNotFoundException, BookingValidationException, ItemNotFoundException, UserNotFoundException {
        return bookingDtoMapper.mapToDto(bookingService.updateStatus(userId, bookingId, approved));
    }

    @GetMapping("/{bookingId}")
    public BookingDto getById(@RequestHeader("X-Sharer-User-Id") long userId,
                              @PathVariable long bookingId) throws BookingNotFoundException, BookingValidationException, ItemNotFoundException, UserNotFoundException {
        return bookingDtoMapper.mapToDto(bookingService.getById(userId, bookingId));
    }

    @GetMapping
    // TODO State - String?
    public Collection<BookingDto> getAllByBookerId(@RequestHeader("X-Sharer-User-Id") long bookerId,
                                                 @RequestParam(required = false, defaultValue = "All") State state) throws UserNotFoundException, BookingValidationException {
        return bookingDtoMapper.mapToDtoCollection(bookingService.getAllByBookerId(bookerId, state));
    }

    @GetMapping("/owner")
    public Collection<BookingDto> getAllByOwnerId(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                                  @RequestParam(required = false, defaultValue = "All") State state) {
        return bookingDtoMapper.mapToDtoCollection(bookingService.getAllByOwnerId(ownerId, state));
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