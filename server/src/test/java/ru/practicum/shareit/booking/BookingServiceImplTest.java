package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import ru.practicum.shareit.booking.dto.BookingDtoMapper;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.exceptions.*;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookingDtoMapper bookingDtoMapper;

    @InjectMocks
    private BookingServiceImpl bookingService;

    User owner = new User(1L, "username", "email@ya.ru", null);
    User booker = new User(10L, "bookername", "booker@ya.ru", null);
    Item item = new Item(1L, "itemname", "dd", owner, true, 8L, null);
    LocalDateTime nowPlus10s = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusSeconds(10);
    LocalDateTime nowPlusMin = LocalDateTime.now().plusMinutes(1).truncatedTo(ChronoUnit.SECONDS);
    BookingInputDto bookingInputDto = BookingInputDto.builder()
            .start(nowPlus10s)
            .end(nowPlusMin)
            .itemId(1L)
            .build();
    Booking booking = new Booking(nowPlus10s, nowPlusMin, new Item(), new User());

    @Test
    public void shouldMakeBooking() throws UserNotFoundException, BookingNotFoundException, BookingValidationException, ItemNotFoundException {
        when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(item));
        when(bookingDtoMapper.mapToBooking(ArgumentMatchers.any(BookingInputDto.class)))
                .thenReturn(booking);
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(booker));
        when(bookingRepository.save(ArgumentMatchers.any(Booking.class)))
                .thenAnswer(invoc -> invoc.getArguments()[0]);

        Booking bookingResult = bookingService.makeBooking(10L,
                bookingInputDto);

        assertThat(bookingResult, notNullValue());
        assertThat(bookingResult, hasProperty("start", equalTo(nowPlus10s)));
        assertThat(bookingResult, hasProperty("end", equalTo(nowPlusMin)));
        assertThat(bookingResult, hasProperty("item", equalTo(item)));
        assertThat(bookingResult, hasProperty("booker", equalTo(booker)));
        assertThat(bookingResult, hasProperty("status", equalTo(Status.WAITING)));
    }

    @Test
    public void shouldFailOnBookerEqualsOwner() {
        when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(item));

        Assertions.assertThrows(BookingNotFoundException.class, () -> bookingService.makeBooking(1L,
                bookingInputDto));
    }

    @Test
    public void shouldFailOnUnavailableItem() {
        item.setAvailable(false);
        when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(item));

        Assertions.assertThrows(BookingValidationException.class, () -> bookingService.makeBooking(10L,
                bookingInputDto));
    }

    @Test
    public void shouldFailOnWrongUser() {
        item.setAvailable(false);
        when(userRepository.existsById(anyLong()))
                .thenReturn(false);

        Assertions.assertThrows(UserNotFoundException.class, () -> bookingService.makeBooking(77L,
                bookingInputDto));
    }

    @Test
    public void shouldUpdateStatus() throws UserNotFoundException, BookingNotFoundException, BookingValidationException, ItemNotFoundException {
        booking.setId(1L);
        booking.setItem(item);
        booking.setBooker(booker);
        when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(booking));
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(item));

        Booking bookingResult = bookingService
                .updateStatus(1L, 1L, true);

        assertThat(bookingResult, notNullValue());
        assertThat(bookingResult, hasProperty("start", equalTo(nowPlus10s)));
        assertThat(bookingResult, hasProperty("end", equalTo(nowPlusMin)));
        assertThat(bookingResult, hasProperty("item", equalTo(item)));
        assertThat(bookingResult, hasProperty("booker", equalTo(booker)));
        assertThat(bookingResult, hasProperty("status", equalTo(Status.APPROVED)));
    }

    @Test
    public void shouldUpdateStatusRejected() throws UserNotFoundException, BookingNotFoundException, BookingValidationException, ItemNotFoundException {
        booking.setId(1L);
        booking.setItem(item);
        booking.setBooker(booker);
        when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(booking));
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(item));

        Booking bookingResult = bookingService
                .updateStatus(1L, 1L, false);

        assertThat(bookingResult, notNullValue());
        assertThat(bookingResult, hasProperty("start", equalTo(nowPlus10s)));
        assertThat(bookingResult, hasProperty("end", equalTo(nowPlusMin)));
        assertThat(bookingResult, hasProperty("item", equalTo(item)));
        assertThat(bookingResult, hasProperty("booker", equalTo(booker)));
        assertThat(bookingResult, hasProperty("status", equalTo(Status.REJECTED)));
    }

    @Test
    public void shouldFailUpdateStatusApproved() {
        booking.setId(1L);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(Status.APPROVED);
        when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(booking));

        Assertions.assertThrows(BookingValidationException.class,
                () -> bookingService
                        .updateStatus(1L, 1L, false));
    }

    @Test
    public void shouldFailUpdateOnNotOwner() {
        booking.setId(1L);
        booking.setItem(item);
        booking.setBooker(booker);
        when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(booking));
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(item));

        Assertions.assertThrows(BookingNotFoundException.class, () ->
                bookingService.updateStatus(10L, 1L, false));
    }

    @Test
    public void shouldGetById() throws UserNotFoundException, BookingNotFoundException, ItemNotFoundException {
        booking.setBooker(booker);
        booking.setItem(item);
        when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(booking));
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(item));

        assertThat(bookingService.getById(1L, 1L), equalTo(booking));
    }

    @Test
    public void shouldFailGetByIdOnWrongUser() throws UserNotFoundException, BookingNotFoundException, ItemNotFoundException {
        booking.setBooker(booker);
        booking.setItem(item);
        when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(booking));
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(item));

        Assertions.assertThrows(BookingNotFoundException.class, () ->
                bookingService.getById(77L, 1L));
    }

    @Test
    public void shouldGetAllByBookerId() throws UserNotFoundException, UnsupportedStateException {
        Page<Booking> page = new PageImpl<>(List.of(booking));

        when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        when(bookingRepository.findAllByBookerId(anyLong(), ArgumentMatchers.any()))
                .thenReturn(page);
        when(bookingRepository.findAllByBookerIdAndEndIsBefore(anyLong(), ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(page);
        when(bookingRepository.findAllByBookerIdAndStartIsBeforeAndEndIsAfter(anyLong(), ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(page);
        when(bookingRepository.findAllByBookerIdAndStartIsAfter(anyLong(), ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(page);
        when(bookingRepository.findAllByBookerIdAndStatusEquals(anyLong(), ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(page);
        when(bookingRepository.findAllByBookerIdAndStatusEquals(anyLong(), ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(page);

        Assertions.assertThrows(UnsupportedStateException.class, () ->
                bookingService.getAllByBookerId(1L, "ILLEGAL", 0, 3));
        assertThat(bookingService.getAllByBookerId(1L, "ALL", 0, 3), equalTo(page.getContent()));
        assertThat(bookingService.getAllByBookerId(1L, "PAST", 0, 3), equalTo(page.getContent()));
        assertThat(bookingService.getAllByBookerId(1L, "CURRENT", 0, 3), equalTo(page.getContent()));
        assertThat(bookingService.getAllByBookerId(1L, "FUTURE", 0, 3), equalTo(page.getContent()));
        assertThat(bookingService.getAllByBookerId(1L, "WAITING", 0, 3), equalTo(page.getContent()));
        assertThat(bookingService.getAllByBookerId(1L, "REJECTED", 0, 3), equalTo(page.getContent()));
    }

    @Test
    public void shouldGetAllByOwnerId() throws UserNotFoundException, UnsupportedStateException {
        Page<Booking> page = new PageImpl<>(List.of(booking));

        when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        when(bookingRepository.findAllByItemOwnerId(anyLong(), ArgumentMatchers.any()))
                .thenReturn(page);
        when(bookingRepository.findAllByItemOwnerIdAndEndIsBefore(anyLong(), ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(page);
        when(bookingRepository.findAllByItemOwnerIdAndStartIsBeforeAndEndIsAfter(anyLong(), ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(page);
        when(bookingRepository.findAllByItemOwnerIdAndStartIsAfter(anyLong(), ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(page);
        when(bookingRepository.findAllByItemOwnerIdAndStatusEquals(anyLong(), ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(page);
        when(bookingRepository.findAllByItemOwnerIdAndStatusEquals(anyLong(), ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(page);

        Assertions.assertThrows(UnsupportedStateException.class, () ->
                bookingService.getAllByBookerId(1L, "ILLEGAL", 0, 3));
        assertThat(bookingService.getAllByOwnerId(1L, "ALL", 0, 3), equalTo(page.getContent()));
        assertThat(bookingService.getAllByOwnerId(1L, "PAST", 0, 3), equalTo(page.getContent()));
        assertThat(bookingService.getAllByOwnerId(1L, "CURRENT", 0, 3), equalTo(page.getContent()));
        assertThat(bookingService.getAllByOwnerId(1L, "FUTURE", 0, 3), equalTo(page.getContent()));
        assertThat(bookingService.getAllByOwnerId(1L, "WAITING", 0, 3), equalTo(page.getContent()));
        assertThat(bookingService.getAllByOwnerId(1L, "REJECTED", 0, 3), equalTo(page.getContent()));
    }
}
