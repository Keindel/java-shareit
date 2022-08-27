package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.exceptions.BookingNotFoundException;
import ru.practicum.shareit.exceptions.BookingValidationException;
import ru.practicum.shareit.exceptions.ItemNotFoundException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.item.dto.ItemWithNearestBookingsDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Transactional
//@Rollback(false)
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceIntegrTest {
    private final UserService userService;
    private final ItemService itemService;
    private final BookingService bookingService;

    @Test
    public void shouldGetAllItemsOfOwner() throws UserNotFoundException, BookingNotFoundException, BookingValidationException, ItemNotFoundException {
        User user = new User(null, "user_name", "useremail@ya.ru", null);
        user = userService.addUser(user);

        assertThat(user, hasProperty("id", equalTo(1L)));

        Item item1 = new Item();
        item1.setName("item1_name");
        item1.setDescription("dd1");
        item1.setOwner(user);
        item1.setAvailable(true);

        item1 = itemService.addItem(1L, item1);
        assertThat(item1, hasProperty("id", equalTo(1L)));

        Item item2 = new Item();
        item2.setName("item2_name");
        item2.setDescription("dd2");
        item2.setOwner(user);
        item2.setAvailable(true);

        item2 = itemService.addItem(1L, item2);
        assertThat(item2, hasProperty("id", equalTo(2L)));

        assertThat(itemService.getAllItemsOfOwner(1L, 0, 3), hasSize(2));

        User booker = new User(null, "booker_name", "bookeremail@ya.ru", null);
        User bookerSaved = userService.addUser(booker);

        BookingInputDto bookingDto1OfItem1 = BookingInputDto.builder()
                .itemId(item1.getId())
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusMinutes(1))
                .build();
        Booking booking1OfItem1 = bookingService.makeBooking(bookerSaved.getId(), bookingDto1OfItem1);

        BookingInputDto bookingDto2OfItem1 = BookingInputDto.builder()
                .itemId(item1.getId())
                .start(LocalDateTime.now().plusMinutes(2))
                .end(LocalDateTime.now().plusMinutes(3))
                .build();
        Booking booking2OfItem1 = bookingService.makeBooking(bookerSaved.getId(), bookingDto2OfItem1);

        BookingInputDto bookingDto1OfItem2 = BookingInputDto.builder()
                .itemId(item2.getId())
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusMinutes(1))
                .build();
        Booking booking1OfItem2 = bookingService.makeBooking(bookerSaved.getId(), bookingDto1OfItem2);

        BookingInputDto bookingDto2OfItem2 = BookingInputDto.builder()
                .itemId(item2.getId())
                .start(LocalDateTime.now().plusMinutes(2))
                .end(LocalDateTime.now().plusMinutes(3))
                .build();
        Booking booking2OfItem2 = bookingService.makeBooking(bookerSaved.getId(), bookingDto2OfItem2);

        Collection<ItemWithNearestBookingsDto> itemsWithBookingsCollection = itemService.getAllItemsOfOwner(1, 0, 3);
        assertThat(itemsWithBookingsCollection, hasSize(2));
        assertThat(itemsWithBookingsCollection.toArray()[0],
                hasProperty("lastBooking"));
        assertThat(itemsWithBookingsCollection.toArray()[0],
                hasProperty("nextBooking"));
        assertThat(itemsWithBookingsCollection.toArray()[1],
                hasProperty("lastBooking"));
        assertThat(itemsWithBookingsCollection.toArray()[1],
                hasProperty("nextBooking"));
    }
}
