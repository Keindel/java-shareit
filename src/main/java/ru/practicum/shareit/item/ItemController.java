package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.CommentValidationException;
import ru.practicum.shareit.exceptions.ItemNotFoundException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoMapper;
import ru.practicum.shareit.item.dto.ItemWithNearestBookingsDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final ItemDtoMapper itemDtoMapper;

    @PostMapping
    public ItemDto addItem(@RequestHeader("X-Sharer-User-Id") long ownerId,
                           @Valid @RequestBody Item item) throws UserNotFoundException {
        return itemDtoMapper.mapToDto(itemService.addItem(ownerId, item));
    }

    @GetMapping
    public Collection<ItemWithNearestBookingsDto> getAllItemsOfOwner(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                                                     @Valid @Min(0) @RequestParam Integer from,
                                                                     @Valid @Min(1) @RequestParam Integer size)
            throws UserNotFoundException {
        return itemService.getAllItemsOfOwner(ownerId, from, size);
    }

    @GetMapping("/{itemId}")
    public ItemWithNearestBookingsDto getById(@PathVariable long itemId,
                                              @RequestHeader("X-Sharer-User-Id") long userId)
            throws ItemNotFoundException {
        return itemService.getById(itemId, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateById(@RequestBody ItemDto itemDto,
                              @RequestHeader("X-Sharer-User-Id") long ownerId,
                              @PathVariable long itemId) throws UserNotFoundException, ItemNotFoundException {
        return itemDtoMapper.mapToDto(itemService.updateItem(itemDto, ownerId, itemId));
    }

    @DeleteMapping("/{itemId}")
    public void deleteById(@RequestHeader("X-Sharer-User-Id") long ownerId,
                           @PathVariable long itemId) throws UserNotFoundException, ItemNotFoundException {
        itemService.deleteById(ownerId, itemId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> searchItems(@RequestParam String text,
                                           @Valid @Min(0) @RequestParam Integer from,
                                           @Valid @Min(1) @RequestParam Integer size) {
        return itemService.searchItems(text, from, size).stream()
                .map(itemDtoMapper::mapToDto).collect(Collectors.toList());
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@PathVariable long itemId,
                                 @Valid @RequestBody CommentDto commentDto,
                                 @RequestHeader("X-Sharer-User-Id") long userId)
            throws UserNotFoundException, CommentValidationException, ItemNotFoundException {
        return (itemService.addComment(itemId, commentDto, userId));
    }
}