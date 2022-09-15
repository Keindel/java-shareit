package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> addItem(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                          @Valid @RequestBody Item item) {
        return itemClient.addItem(ownerId, item);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItemsOfOwner(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                                                     @Valid @Min(0) @RequestParam(required = false) Integer from,
                                                                     @Valid @Min(1) @RequestParam(required = false) Integer size) {
        return itemClient.getAllItemsOfOwner(ownerId, from, size);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getById(@PathVariable long itemId,
                                              @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemClient.getById(itemId, userId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateById(@RequestBody ItemDto itemDto,
                              @RequestHeader("X-Sharer-User-Id") long ownerId,
                              @PathVariable long itemId) {
        return itemClient.updateById(itemDto, ownerId, itemId);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Object> deleteById(@RequestHeader("X-Sharer-User-Id") long ownerId,
                           @PathVariable long itemId) {
        return itemClient.deleteById(ownerId, itemId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestParam String text,
                                           @Valid @Min(0) @RequestParam(required = false) Integer from,
                                           @Valid @Min(1) @RequestParam(required = false) Integer size) {
        return itemClient.searchItems(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@PathVariable long itemId,
                                 @Valid @RequestBody CommentDto commentDto,
                                 @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemClient.addComment(itemId, commentDto, userId);
    }
}
