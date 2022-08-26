package ru.practicum.shareit.requests;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.ItemRequestNotFoundException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.requests.dto.ItemRequestInputDto;
import ru.practicum.shareit.requests.dto.ItemRequestMapper;
import ru.practicum.shareit.requests.dto.ItemRequestWithResponsesDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService itemRequestService;
    private final ItemRequestMapper itemRequestMapper;

    @PostMapping
    public ItemRequest add(@RequestHeader(value = "X-Sharer-User-Id") long requesterId,
                           @Valid @RequestBody ItemRequestInputDto itemRequestInputDto) throws UserNotFoundException {
        return itemRequestService.add(requesterId, itemRequestInputDto);
    }

    @GetMapping("/{requestId}")
    public ItemRequestWithResponsesDto getById(@RequestHeader(value = "X-Sharer-User-Id") long userId,
                                               @PathVariable long requestId)
            throws UserNotFoundException, ItemRequestNotFoundException {
        return itemRequestMapper.toDtoWithResponses(itemRequestService.getById(userId, requestId));
    }

    @GetMapping
    public Collection<ItemRequestWithResponsesDto> getAllByUserId(@RequestHeader(value = "X-Sharer-User-Id") long requesterId)
            throws UserNotFoundException {
        return itemRequestService.getAllByRequesterId(requesterId)
                .stream().map(itemRequestMapper::toDtoWithResponses).collect(Collectors.toList());
    }

    @GetMapping("/all")
    public Collection<ItemRequestWithResponsesDto> getAllInPages(@Valid @Min(0) @RequestParam int from,
                                                                 @Valid @Min(1) @RequestParam int size) {
        return itemRequestService.getAllInPages(from, size)
                .stream().map(itemRequestMapper::toDtoWithResponses).collect(Collectors.toList());
    }
}