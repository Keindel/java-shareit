package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.exceptions.UserNotFoundException;

import java.util.Collection;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Collection<Item> findAllByOwnerIdOrderByIdAsc(long ownerId) throws UserNotFoundException;

    @Query("SELECT it FROM Item it " +
            "WHERE it.available=true " +
            "AND (LOWER(it.name) LIKE LOWER(CONCAT('%', :text, '%') ) " +
            "OR LOWER(it.description) LIKE LOWER(concat('%', :text, '%') ))")
    Collection<Item> searchItems(String text);
}
