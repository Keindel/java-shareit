package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.CommentValidationException;
import ru.practicum.shareit.exceptions.ItemNotFoundException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoMapper;
import ru.practicum.shareit.item.dto.ItemWithNearestBookingsDto;

import javax.validation.Valid;
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
                           @RequestBody @Valid Item item) throws UserNotFoundException {
        return itemDtoMapper.mapToDto(itemService.addItem(ownerId, item));
    }

    @GetMapping
    public Collection<ItemWithNearestBookingsDto> getAllItemsOfOwner(@RequestHeader("X-Sharer-User-Id") long ownerId)
            throws UserNotFoundException {
        return itemService.getAllItemsOfOwner(ownerId);
    }
    /*Отзывы можно будет увидеть по двум эндпоинтам — по GET /items/{itemId} для одной конкретной вещи
     и по GET /items для всех вещей данного пользователя.*/

    @GetMapping("/{itemId}")
    public ItemDto getById(@PathVariable long itemId) throws UserNotFoundException, ItemNotFoundException {
        return itemService.getById(itemId);
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
    public Collection<ItemDto> searchItems(@RequestParam String text) {
        return itemService.searchItems(text).stream()
                .map(itemDtoMapper::mapToDto).collect(Collectors.toList());
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@PathVariable long itemId,
                                 @RequestBody CommentDto commentDto,
                                 @RequestHeader("X-Sharer-User-Id") long userId)
            throws UserNotFoundException, CommentValidationException, ItemNotFoundException {
        return (itemService.addComment(itemId, commentDto, userId));
    }
}