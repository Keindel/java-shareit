package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;

    @Override
    public Item addItem(long ownerId, Item item) {
        return itemRepository.addItem(ownerId, item);
    }

    @Override
    public Collection<Item> getAllItemsOfOwner(long ownerId) {
        return itemRepository.getAllItemsOfOwner(ownerId);
    }

    @Override
    public Item getById(long ownerId, long id) {
        return itemRepository.getById(ownerId, id);
    }

    @Override
    public Item updateItem(ItemDto itemDto, long ownerId, long itemId) {
        Item itemFromRepo = itemRepository.getById(ownerId, itemId);

        Item itemFromRepoCopy = new Item(itemFromRepo.getItemId(), itemFromRepo.getItemName(),
                itemFromRepo.getItemDescription(), itemFromRepo.getOwnerId(),
                itemFromRepo.getAvailable(), itemFromRepo.getBookingsIds());
        String nameUpdate = itemDto.getItemName();
        String descriptionUpdate = itemDto.getItemDescription();
        Boolean availabilityUpdate = itemDto.getAvailable();
        if (nameUpdate != null) {
            itemFromRepoCopy.setItemName(nameUpdate);
        }
        if (descriptionUpdate != null) {
            itemFromRepoCopy.setItemDescription(descriptionUpdate);
        }
        if (availabilityUpdate != null) {
            itemFromRepoCopy.setAvailable(availabilityUpdate);
        }
        return itemRepository.updateItem(itemFromRepoCopy, ownerId, itemId);
    }

    @Override
    public void deleteById(long ownerId, long id) {
        itemRepository.deleteById(ownerId, id);
    }
}
