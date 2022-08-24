package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.exceptions.UserNotFoundException;

import java.util.Collection;

public interface ItemRepository extends JpaRepository<Item, Long> {
//    Item addItem(long ownerId, Item item) throws UserNotFoundException;
//
    Collection<Item> findAllByOwnerIdOrderByIdAsc(long ownerId) throws UserNotFoundException;
    Collection<Item> findByNameContainingIgnoreCase(String text);
    Collection<Item> findByDescriptionContainingIgnoreCase(String text);

    @Query("SELECT it FROM Item it " +
            "WHERE it.available=true " +
            "AND (LOWER(it.name) LIKE LOWER(CONCAT('%', :text, '%') ) " +
            "OR LOWER(it.description) LIKE LOWER(concat('%', :text, '%') ))")
    Collection<Item> searchItems(String text);
    /*
        return usersItems.values().stream()
                .flatMap(Collection::stream)
                .filter(Item::getAvailable)
                .filter(item -> item.getName().toLowerCase().contains(text.toLowerCase())
                        || item.getDescription().toLowerCase().contains(text.toLowerCase()))
                .collect(Collectors.toList());
      */
//
//    Item getById(long id) throws ItemNotFoundException;
//
//    Item updateItem(Item item, long ownerId, long itemId) throws UserNotFoundException, ItemNotFoundException;
//
//    void deleteById(long ownerId, long id) throws UserNotFoundException;
//
//    Collection<Item> searchItems(String text);
}
