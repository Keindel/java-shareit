package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.ItemNotFoundException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserRepository;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public Item addItem(long ownerId, Item item) throws UserNotFoundException {
        if (!userRepository.existsById(ownerId)) {
            throw new UserNotFoundException();
        }
        item.setOwnerId(ownerId);
        return itemRepository.save(item);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public Collection<Item> getAllItemsOfOwner(long ownerId) throws UserNotFoundException {
        return itemRepository.findAllByOwnerId(ownerId);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public Item getById(long id) throws ItemNotFoundException {
        return itemRepository.findById(id).orElseThrow(ItemNotFoundException::new);
    }

    @Override
    public Item updateItem(ItemDto itemDto, long ownerId, long itemId) throws UserNotFoundException, ItemNotFoundException {
        Item itemFromRepo = itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);
        validateItemBelonging(ownerId, itemId);

//        Item itemFromRepoCopy = new Item(itemFromRepo.getId(), itemFromRepo.getName(),
//                itemFromRepo.getDescription(), itemFromRepo.getOwnerId(),
//                itemFromRepo.getAvailable(), itemFromRepo.getRequestId(), itemFromRepo.getBookingsIds());
        String nameUpdate = itemDto.getName();
        String descriptionUpdate = itemDto.getDescription();
        Boolean availabilityUpdate = itemDto.getAvailable();
        if (nameUpdate != null) {
            itemFromRepo.setName(nameUpdate);
        }
        if (descriptionUpdate != null) {
            itemFromRepo.setDescription(descriptionUpdate);
        }
        if (availabilityUpdate != null) {
            itemFromRepo.setAvailable(availabilityUpdate);
        }
        return itemRepository.save(itemFromRepo);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    void validateItemBelonging(long ownerId, long itemId) throws UserNotFoundException, ItemNotFoundException {
        if (!userRepository.findById(ownerId).orElseThrow(UserNotFoundException::new)
                .getItemsIdsForSharing().contains(itemId)) {
            throw new ItemNotFoundException("item does not belong to this user");
        }
    }

    @Override
    public void deleteById(long ownerId, long id) throws UserNotFoundException, ItemNotFoundException {
        validateItemBelonging(ownerId, id);
        itemRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<Item> searchItems(String text) {
        if (text.isBlank()) {
            return List.of();
        }
//        Set<Item> foundItems = new HashSet<>();
//        foundItems.addAll(itemRepository.findByNameContainingIgnoreCase(text));
//        foundItems.addAll(itemRepository.findByDescriptionContainingIgnoreCase(text));
//        return foundItems;
        return itemRepository.searchItems(text);
    }
}
