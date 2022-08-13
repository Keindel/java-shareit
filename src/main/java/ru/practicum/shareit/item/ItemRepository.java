package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.exceptions.ItemNotFoundException;
import ru.practicum.shareit.exceptions.UserNotFoundException;

import java.util.Collection;

public interface ItemRepository extends JpaRepository<Item, Long> {
//    Item addItem(long ownerId, Item item) throws UserNotFoundException;
//
    Collection<Item> findAllByOwnerId(long ownerId) throws UserNotFoundException;
    Collection<Item> findByNameContainingIgnoreCase(String text);
    Collection<Item> findByDescriptionContainingIgnoreCase(String text);
//
//    Item getById(long id) throws ItemNotFoundException;
//
//    Item updateItem(Item item, long ownerId, long itemId) throws UserNotFoundException, ItemNotFoundException;
//
//    void deleteById(long ownerId, long id) throws UserNotFoundException;
//
//    Collection<Item> searchItems(String text);
}
