package ru.practicum.shareit.item;

import ru.practicum.shareit.exceptions.CommentValidationException;
import ru.practicum.shareit.exceptions.ItemNotFoundException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithNearestBookingsDto;

import java.util.Collection;
import java.util.List;

public interface ItemService {
    Item addItem(long ownerId, Item item) throws UserNotFoundException;

    Collection<ItemWithNearestBookingsDto> getAllItemsOfOwner(long ownerId, Integer from, Integer size) throws UserNotFoundException;

    ItemWithNearestBookingsDto getById(long id, long userId) throws ItemNotFoundException;

    Item updateItem(ItemDto itemDto, long ownerId, long itemId) throws UserNotFoundException, ItemNotFoundException;

    void deleteById(long ownerId, long id) throws UserNotFoundException, ItemNotFoundException;

    Collection<Item> searchItems(String text, Integer from, Integer size);

    CommentDto addComment(long itemId, CommentDto commentDto, long userId) throws ItemNotFoundException, UserNotFoundException, CommentValidationException;

    List<Item> findAllByRequestId(long requestId);
}
