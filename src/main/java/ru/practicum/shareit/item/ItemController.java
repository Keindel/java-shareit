package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoMapper;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemDto addItem(@RequestBody @Valid Item item) {
        return ItemDtoMapper.mapToDto(itemService.addItem(item));
    }

    @GetMapping
    public ItemDto getById(@PathVariable long itemId) {
        return ItemDtoMapper.mapToDto(itemService.getById(itemId));
    }

    @PatchMapping
    public ItemDto updateItem(@RequestBody @Valid Item item) {
        return ItemDtoMapper.mapToDto(itemService.updateItem(item));
    }

    @DeleteMapping
    public void deleteById(@PathVariable long userId) {
        itemService.deleteById(userId);
    }
    //search item

    //tell items for sharing

    //add item per request

}

/*
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