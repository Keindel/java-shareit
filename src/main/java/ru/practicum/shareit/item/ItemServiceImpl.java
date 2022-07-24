package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ItemNotFoundException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;

    @Override
    public Item addItem(long ownerId, Item item) throws UserNotFoundException {
        return itemRepository.addItem(ownerId, item);
    }

    @Override
    public Collection<Item> getAllItemsOfOwner(long ownerId) throws UserNotFoundException {
        return itemRepository.getAllItemsOfOwner(ownerId);
    }

    @Override
    public Item getById(long id) throws ItemNotFoundException {
        return itemRepository.getById(id);
    }

    @Override
    public Item updateItem(ItemDto itemDto, long ownerId, long itemId) throws UserNotFoundException, ItemNotFoundException {
        Item itemFromRepo = itemRepository.getById(itemId);

        Item itemFromRepoCopy = new Item(itemFromRepo.getId(), itemFromRepo.getName(),
                itemFromRepo.getDescription(), itemFromRepo.getOwnerId(),
                itemFromRepo.getAvailable(), itemFromRepo.getBookingsIds());
        String nameUpdate = itemDto.getName();
        String descriptionUpdate = itemDto.getDescription();
        Boolean availabilityUpdate = itemDto.getAvailable();
        if (nameUpdate != null) {
            itemFromRepoCopy.setName(nameUpdate);
        }
        if (descriptionUpdate != null) {
            itemFromRepoCopy.setDescription(descriptionUpdate);
        }
        if (availabilityUpdate != null) {
            itemFromRepoCopy.setAvailable(availabilityUpdate);
        }
        return itemRepository.updateItem(itemFromRepoCopy, ownerId, itemId);
    }

    @Override
    public void deleteById(long ownerId, long id) throws UserNotFoundException {
        itemRepository.deleteById(ownerId, id);
    }

    @Override
    public Collection<Item> searchItems(String text) {
        return itemRepository.searchItems(text);
    }
}
