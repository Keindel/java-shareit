package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDtoMapper;
import ru.practicum.shareit.exceptions.CommentValidationException;
import ru.practicum.shareit.exceptions.ItemNotFoundException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.CommentDtoMapper;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithNearestBookingsDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemServiceImplTest {
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

    User user = new User(1L, "username", "email@ya.ru", null);
    User booker = new User(10L, "bookername", "booker@ya.ru", null);
    Item item = new Item(1L, "itemname", "dd", user, true, 8L, null);
    Booking booking = new Booking(LocalDateTime.now().minusMinutes(2), LocalDateTime.now().minusMinutes(1), item, booker);

    ItemWithNearestBookingsDto itemWithNearestBookingsDto = ItemWithNearestBookingsDto.builder()
            .id(1L)
            .name("itemname")
            .description("dd")
            .build();

    ItemDto itemDto = ItemDto.builder()
            .id(1L)
            .description("ddupdate")
            .name("itemnameupdate")
            .build();

    CommentDto commentDto = CommentDto.builder()
            .text("text")
            .authorName("username")
            .build();

    @Test
    public void shouldFailGetAllOnWrongOwnerId() throws UserNotFoundException {
        when(itemRepository.findAllByOwnerId(anyLong(), any()))
                .thenThrow(UserNotFoundException.class);

        Assertions.assertThrows(UserNotFoundException.class, () -> {
            itemService.getAllItemsOfOwner(1, 0, 1);
        });
    }

    @Test
    public void shouldAddItem() throws UserNotFoundException {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(new User()));
        when(itemRepository.save(any(Item.class)))
                .thenReturn(item);

        assertThat(itemService.addItem(1L, item), equalTo(item));
    }

    @Test
    public void shouldGetById() throws ItemNotFoundException {
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(item));

        assertThat(itemService.getById(1L, 8L), equalTo(itemWithNearestBookingsDto));
    }

    @Test
    public void shouldGetByIdWithNearestBookings() throws ItemNotFoundException {
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(item));

        assertThat(itemService.getById(1L, 1L), equalTo(itemWithNearestBookingsDto));
    }

    @Test
    public void shouldUpdateById() throws UserNotFoundException, ItemNotFoundException {
        user.setItemsForSharing(List.of(item));
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));
        when(itemRepository.save(any(Item.class)))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);

        Item updatedItem = itemService.updateItem(itemDto, 1L, 1L);

        assertThat(updatedItem, hasProperty("id", equalTo(1L)));
        assertThat(updatedItem, hasProperty("name", equalTo(itemDto.getName())));
        assertThat(updatedItem, hasProperty("description", equalTo(itemDto.getDescription())));
        assertThat(updatedItem, hasProperty("available", equalTo(item.getAvailable())));
    }

    @Test
    public void shouldFailUpdateById() {
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(item));

        Assertions.assertThrows(UserNotFoundException.class, () ->
                itemService.updateItem(itemDto, 8L, 1L));
    }

    @Test
    public void shouldDeleteById() throws UserNotFoundException, ItemNotFoundException {
        user.setItemsForSharing(List.of(item));
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));

        itemService.deleteById(1L, 1L);
    }

    @Test
    public void shouldSearchItems() {
        when(itemRepository.searchItems(anyString(), any()))
                .thenReturn(new PageImpl<>(List.of(item)));

        Collection<Item> searchResult = itemService.searchItems("dd", 0, 3);
        assertThat(searchResult, hasSize(1));
        assertThat(searchResult, equalTo(List.of(item)));
        assertThat(searchResult, hasItem(item));
    }

    @Test
    public void shouldAddComment() throws UserNotFoundException, CommentValidationException, ItemNotFoundException {
        item.setBookings(List.of(booking));

        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(item));
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(booker));
        when(commentRepository.save(ArgumentMatchers.any(Comment.class)))
                .thenAnswer(invoc -> invoc.getArguments()[0]);
        when(commentDtoMapper.toDto(ArgumentMatchers.any(Comment.class)))
                .thenReturn(commentDto);

        assertThat(itemService.addComment(1L, commentDto, 10L), equalTo(commentDto));
    }

    @Test
    public void shouldSearchEmptyListWhenTextIsBlank() {
        when(itemRepository.searchItems(anyString(), any()))
                .thenReturn(new PageImpl<>(List.of()));

        Collection<Item> searchResult = itemService.searchItems("dd", 0, 3);
        assertThat(searchResult, hasSize(0));
    }

    @Test
    public void shouldfindAllByRequestId() {
        when(itemRepository.findAllByRequestId(anyLong()))
                .thenReturn(List.of(item));

        Collection<Item> result = itemService.findAllByRequestId(8);
        assertThat(result, hasSize(1));
        assertThat(result, equalTo(List.of(item)));
        assertThat(result, hasItem(item));
    }
}