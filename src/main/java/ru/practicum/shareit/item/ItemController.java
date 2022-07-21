package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoMapper;

import javax.validation.Valid;
import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto addItem(@RequestHeader("X-Sharer-User-Id") long ownerId,
                           @RequestBody @Valid Item item) {
        return ItemDtoMapper.mapToDto(itemService.addItem(ownerId, item));
    }

    @GetMapping("/{itemId}")
    public Collection<ItemDto> getAllItemsOfOwner(@RequestHeader("X-Sharer-User-Id") long ownerId) {
        return itemService.getAllItemsOfOwner(ownerId).stream()
                .map(ItemDtoMapper::mapToDto).collect(Collectors.toList());
    }

    @GetMapping("/{itemId}")
    public ItemDto getById(@RequestHeader("X-Sharer-User-Id") long ownerId,
                           @PathVariable long itemId) {
        return ItemDtoMapper.mapToDto(itemService.getById(ownerId, itemId));
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateById(@RequestBody ItemDto itemDto,
                              @RequestHeader("X-Sharer-User-Id") long ownerId,
                              @PathVariable long itemId) {
        return ItemDtoMapper.mapToDto(itemService.updateItem(itemDto, ownerId, itemId));
    }

    @DeleteMapping
    public void deleteById(@RequestHeader("X-Sharer-User-Id") long ownerId,
                           @PathVariable long itemId) {
        itemService.deleteById(ownerId, itemId);
    }


    //search item

    //tell items for sharing

    //add item per request
}

/*
Изменить можно название, описание и статус доступа к аренде.
Редактировать вещь может только её владелец.


Вам нужно реализовать добавление новых вещей, их редактирование, просмотр списка вещей и поиск

1.	возможность рассказывать, какими вещами они готовы поделиться
* ??????? 2.	находить нужную вещь и брать её в аренду на какое-то время
5.	По запросу можно будет добавлять новые вещи для шеринга
6.	Пользователь, который добавляет в приложение новую вещь, будет считаться ее владельцем
7.	При добавлении вещи должна быть возможность указать её краткое название и добавить небольшое описание
8.	Также у вещи обязательно должен быть статус — доступна ли она для аренды
9.	Статус должен проставлять владелец
10.	Для поиска вещей должен быть организован поиск
* */