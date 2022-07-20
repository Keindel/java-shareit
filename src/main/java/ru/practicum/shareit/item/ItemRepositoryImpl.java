package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ItemRepositoryImpl implements ItemRepository{
    private static long itemId = 0;
    private final Map<Long, List<Item>> usersItems = new HashMap<>();

    @Override
    public Item addItem(Item item) {
        return null;
    }

    @Override
    public Item getById(long id) {
        return null;
    }

    @Override
    public Item updateItem(Item item) {
        return null;
    }

    @Override
    public void deleteById(long id) {

    }
}

/*
* просмотр списка вещей
* поиск вещи
* */