package ru.practicum.shareit.requests;

import ru.practicum.shareit.exceptions.ItemRequestNotFoundException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.requests.dto.ItemRequestInputDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequest add(long requesterId, ItemRequestInputDto itemRequestInputDto) throws UserNotFoundException;

    ItemRequest getById(long userId, long requestId) throws ItemRequestNotFoundException, UserNotFoundException;

    List<ItemRequest> getAllByRequesterId(long requesterId) throws UserNotFoundException;

    List<ItemRequest> getAllInPages(int from, int size);
}
