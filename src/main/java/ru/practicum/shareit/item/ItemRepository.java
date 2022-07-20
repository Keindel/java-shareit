package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

public interface ItemRepository {
    Item addItem(Item item);
    Item getById(long id);
    Item updateItem(Item item);
    void deleteById(long id);
}
