package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserRepository;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepository{
    private static long itemId = 0;
    private final Map<Long, Set<Item>> usersItems = new HashMap<>();
    private final UserRepository userRepository;

    private void ifNoSuchUserIsPresentThrowEx(long userId) {
        userRepository.getById(userId);
    }

    @Override
    public Item addItem(long ownerId, Item item) {
        ifNoSuchUserIsPresentThrowEx(ownerId);
        itemId++;
        item.setItemId(itemId);
//        List<Item> userItemsList = usersItems.get(ownerId);
//        if (userItemsList == null) {
//            userItemsList = new ArrayList<>();
//        }
//        userItemsList.add(item);
//        usersItems.put(ownerId, userItemsList);
        usersItems.compute(ownerId, (userId, itemsList) -> {
            if (itemsList == null) {
                itemsList = new HashSet<>();
            }
            itemsList.add(item);
            return itemsList;
        });
        return item;
    }

    @Override
    public Collection<Item> getAllItemsOfOwner(long ownerId) {
        ifNoSuchUserIsPresentThrowEx(ownerId);
        return usersItems.get(ownerId);
    }

    @Override
    public Item getById(long ownerId, long id) {
        ifNoSuchUserIsPresentThrowEx(ownerId);
        return usersItems.get(ownerId).stream().filter(item -> item.getItemId() == id).findFirst().orElseThrow();
    }

    @Override
    public Item updateItem(Item item, long ownerId, long itemId) {
        usersItems.computeIfPresent(ownerId, (userId, itemSet) -> {
            if (itemSet.remove(item)) {
                itemSet.add(item);
            } else {
                throw new NoSuchElementException("No such item in repo OR this item does not belong to specified owner");
            }
            return itemSet;
        });
        return null;
    }

    @Override
    public void deleteById(long ownerId, long id) {
        ifNoSuchUserIsPresentThrowEx(ownerId);
        usersItems.get(ownerId).remove(Item.builder().itemId(id).build());
    }
}

/*
* просмотр списка вещей
* поиск вещи
* */