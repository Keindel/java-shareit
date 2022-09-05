package ru.practicum.shareit.requests;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.requests.dto.ItemRequestInputDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> add(@RequestHeader(value = "X-Sharer-User-Id") long requesterId,
                                      @Valid @RequestBody ItemRequestInputDto itemRequestInputDto) {
        return itemRequestClient.add(requesterId, itemRequestInputDto);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getById(@RequestHeader(value = "X-Sharer-User-Id") long userId,
                                          @PathVariable long requestId) {
        return itemRequestClient.getById(userId, requestId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllByUserId(@RequestHeader(value = "X-Sharer-User-Id") long requesterId) {
        return itemRequestClient.getAllByUserId(requesterId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllOfOthersInPages(@RequestHeader(value = "X-Sharer-User-Id") long userId,
                                                        @Valid @Min(0) @RequestParam(required = false) Integer from,
                                                        @Valid @Min(1) @RequestParam(required = false) Integer size) {
        return itemRequestClient.getAllOfOthersInPages(userId, from, size);
    }
}
