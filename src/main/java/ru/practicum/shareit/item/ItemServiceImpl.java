package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDtoMapper;
import ru.practicum.shareit.exceptions.CommentValidationException;
import ru.practicum.shareit.exceptions.ItemNotFoundException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.CommentDtoMapper;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoMapper;
import ru.practicum.shareit.item.dto.ItemWithNearestBookingsDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.util.CustomPageable;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemDtoMapper itemDtoMapper;
    private final CommentDtoMapper commentDtoMapper;
    private final BookingDtoMapper bookingDtoMapper;

    @Override
    public Item addItem(long ownerId, Item item) throws UserNotFoundException {
        User owner = userRepository.findById(ownerId).orElseThrow(UserNotFoundException::new);
        item.setOwner(owner);
        return itemRepository.save(item);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<ItemWithNearestBookingsDto> getAllItemsOfOwner(long ownerId, Integer from, Integer size)
            throws UserNotFoundException {
        LocalDateTime now = LocalDateTime.now();

        Pageable page = CustomPageable.of(from, size, Sort.sort(Item.class).by(Item::getId).ascending());

        return itemRepository.findAllByOwnerId(ownerId, page).stream().map(item ->
                ItemWithNearestBookingsDto.builder()
                        .id(item.getId())
                        .name(item.getName())
                        .available(item.getAvailable())
                        .description(item.getDescription())
                        .lastBooking(bookingDtoMapper.mapToBookerIdDto(bookingRepository
                                .getFirstByItemIdAndStartIsBeforeOrderByStartDesc(item.getId(), now)))
                        .nextBooking(bookingDtoMapper.mapToBookerIdDto(bookingRepository
                                .getFirstByItemIdAndStartIsAfterOrderByStartAsc(item.getId(), now)))
                        .comments((commentRepository.findAllByItemId(item.getId()))
                                .stream().map(commentDtoMapper::toDto).collect(Collectors.toList()))
                        .build()).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ItemWithNearestBookingsDto getById(long id, long userId) throws ItemNotFoundException {
        LocalDateTime now = LocalDateTime.now();

        Item item = itemRepository.findById(id).orElseThrow(ItemNotFoundException::new);
        ItemWithNearestBookingsDto itemWithNearestBookingsDto = ItemWithNearestBookingsDto.builder()
                .id(item.getId())
                .name(item.getName())
                .available(item.getAvailable())
                .description(item.getDescription())
                .comments((commentRepository.findAllByItemId(item.getId()))
                        .stream().map(commentDtoMapper::toDto).collect(Collectors.toList()))
                .build();
        if (item.getOwner().getId() != userId) {
            return itemWithNearestBookingsDto;
        }
        itemWithNearestBookingsDto.setLastBooking(bookingDtoMapper.mapToBookerIdDto(bookingRepository
                .getFirstByItemIdAndStartIsBeforeOrderByStartDesc(item.getId(), now)));
        itemWithNearestBookingsDto.setNextBooking(bookingDtoMapper.mapToBookerIdDto(bookingRepository
                .getFirstByItemIdAndStartIsAfterOrderByStartAsc(item.getId(), now)));
        return itemWithNearestBookingsDto;
    }

    @Override
    public Item updateItem(ItemDto itemDto, long ownerId, long itemId) throws UserNotFoundException, ItemNotFoundException {
        Item itemFromRepo = itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);
        validateItemBelonging(ownerId, itemId);

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
                .getItemsForSharing()
                .contains(itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new))) {
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
    public Collection<Item> searchItems(String text, Integer from, Integer size) {
        if (text.isBlank()) {
            return List.of();
        }

        Pageable page = CustomPageable.of(from, size, Sort.sort(Item.class).by(Item::getId).ascending());

        return itemRepository.searchItems(text, page).getContent();
    }

    public CommentDto addComment(long itemId, CommentDto commentDto, long userId)
            throws ItemNotFoundException, UserNotFoundException, CommentValidationException {
        Item item = itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Optional<Booking> firstBookingByTheUser = item.getBookings().stream()
                .filter(booking -> booking.getBooker().equals(user)).min(Comparator.comparing(Booking::getEnd));
        if (firstBookingByTheUser.isEmpty()) {
            throw new CommentValidationException("current User had NOT BOOKED this item yet");
        } else if (firstBookingByTheUser.get().getEnd().isAfter(LocalDateTime.now())) {
            throw new CommentValidationException("current User had NOT COMPLETED BOOKING yet");
        }
        Comment comment = new Comment();
        comment.setItem(item);
        comment.setText(commentDto.getText());
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());
        return commentDtoMapper.toDto(commentRepository.save(comment));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Item> findAllByRequestId(long requestId) {
        return itemRepository.findAllByRequestId(requestId);
    }
}
