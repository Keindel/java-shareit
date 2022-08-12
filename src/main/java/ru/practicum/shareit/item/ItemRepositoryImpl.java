package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.ItemNotFoundException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepository {
    private long itemId = 0;
    private final Map<Long, Set<Item>> usersItems = new HashMap<>();
    private final UserRepository userRepository;

    private void validateUserPresence(long userId) throws UserNotFoundException {
        userRepository.getById(userId);
    }

    @Override
    public Item addItem(long ownerId, Item item) throws UserNotFoundException {
        validateUserPresence(ownerId);
        itemId++;
        item.setId(itemId);
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
    public Collection<Item> getAllItemsOfOwner(long ownerId) throws UserNotFoundException {
        validateUserPresence(ownerId);
        return usersItems.get(ownerId);
    }

    @Override
    public Item getById(long id) throws ItemNotFoundException {
        return usersItems.values().stream().flatMap(Collection::stream).filter(item -> item.getId() == id)
                .findFirst()
                .orElseThrow(ItemNotFoundException::new);
    }

    @Override
    public Item updateItem(Item item, long ownerId, long itemId) throws UserNotFoundException, ItemNotFoundException {
        validateUserPresence(ownerId);
        if (usersItems.get(ownerId) == null) {
            throw new ItemNotFoundException("user does not have any items");
        }
        if (!usersItems.get(ownerId).contains(item)) {
            throw new ItemNotFoundException("No such item in repo OR this item does not belong to specified owner");
        }
        usersItems.computeIfPresent(ownerId, (userId, itemSet) -> {
            if (itemSet.remove(item)) {
                itemSet.add(item);
            }
            return itemSet;
        });
        return getById(itemId);
    }

    @Override
    public void deleteById(long ownerId, long id) throws UserNotFoundException {
        validateUserPresence(ownerId);
        Item itemToDelete = new Item();
        itemToDelete.setId(id);
        usersItems.get(ownerId).remove(itemToDelete);
    }

    @Override
    public Collection<Item> searchItems(String text) {
        if (text.isBlank()) {
            return List.of();
        }
        return usersItems.values().stream()
                .flatMap(Collection::stream)
                .filter(Item::getAvailable)
                .filter(item -> item.getName().toLowerCase().contains(text.toLowerCase())
                        || item.getDescription().toLowerCase().contains(text.toLowerCase()))
                .collect(Collectors.toList());
    }
}