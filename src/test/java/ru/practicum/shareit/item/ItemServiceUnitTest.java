package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDtoMapper;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.item.comment.CommentDtoMapper;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.user.UserRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

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
}
