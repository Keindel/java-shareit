package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

public interface ItemRepository {
    Item addItem(long ownerId, Item item);

    Collection<Item> getAllItemsOfOwner(long ownerId);
    Item getById(long ownerId, long id);
    Item updateItem(Item item, long ownerId, long itemId);
    void deleteById(long ownerId, long id);
}
