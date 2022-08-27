package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDtoMapper;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.item.comment.CommentDtoMapper;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ItemServiceUnitTest {
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private CommentDtoMapper commentDtoMapper;
    @Mock
    private BookingDtoMapper bookingDtoMapper;

    @InjectMocks
    private ItemServiceImpl itemService;

    @Test
    public void shouldFailGetAllOnWrongOwnerId() throws UserNotFoundException {
        Mockito.when(itemRepository.findAllByOwnerId(anyLong(), any()))
                .thenThrow(UserNotFoundException.class);

        Assertions.assertThrows(UserNotFoundException.class, () -> {
            itemService.getAllItemsOfOwner(1, 0, 1);
        });
    }

//    @Test
//    public void shouldGetAllItemsOfOwner() throws UserNotFoundException {
//        User user = new User();
//
//        Item item1 = new Item();
//        item1.setId(1L);
//        item1.setDescription("dd1");
//        item1.setOwner(user);
//        item1.setAvailable(true);
//
//        Item item2 = new Item();
//        item2.setId(2L);
//        item2.setDescription("dd2");
//        item2.setOwner(user);
//        item2.setAvailable(true);
//
//        List<Item> itemList = List.of(item1, item2);
//
//        Mockito.when(itemRepository.findAllByOwnerId(anyLong(), ArgumentMatchers.any(Pageable.class)))
//                .thenReturn(new PageImpl<>(itemList));
//        Mockito.when(bookingRepository.getFirstByItemIdAndStartIsBeforeOrderByStartDesc(anyLong(),
//                        ArgumentMatchers.any(LocalDateTime.class)))
//                .thenReturn();
//        Mockito.when(bookingRepository.getFirstByItemIdAndStartIsAfterOrderByStartAsc(anyLong(),
//                        ArgumentMatchers.any(LocalDateTime.class)))
//                .thenReturn();
//        Mockito.when(commentRepository.findAllByItemId())
//                .thenReturn();
//
//        assertThat(itemService.getAllItemsOfOwner(2, 0, 2), equalTo());
//
//        Mockito.verify(itemRepository, Mockito.times(1))
//                .findAllByOwnerId(2, ArgumentMatchers.any(Pageable.class));
//        Mockito.verify(bookingRepository, Mockito.times(2))
//                .getFirstByItemIdAndStartIsBeforeOrderByStartDesc();
//        Mockito.verify(bookingRepository, Mockito.times(2))
//                .getFirstByItemIdAndStartIsAfterOrderByStartAsc();
//    }
}
