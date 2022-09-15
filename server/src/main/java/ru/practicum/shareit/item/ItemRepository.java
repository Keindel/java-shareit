package ru.practicum.shareit.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.exceptions.UserNotFoundException;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Page<Item> findAllByOwnerId(long ownerId, Pageable page) throws UserNotFoundException;

    @Query("SELECT it FROM Item it " +
            "WHERE it.available=true " +
            "AND (LOWER(it.name) LIKE LOWER(CONCAT('%', :text, '%') ) " +
            "OR LOWER(it.description) LIKE LOWER(concat('%', :text, '%') ))")
    Page<Item> searchItems(String text, Pageable page);

    List<Item> findAllByRequestId(long requestId);
}
