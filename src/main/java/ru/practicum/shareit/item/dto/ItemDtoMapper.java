package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.Item;


public class ItemDtoMapper {
    public static ItemDto mapToDto(Item item) {
        return ItemDto.builder()
                .itemId(item.getItemId())
                .itemName(item.getItemName())
                .itemDescription(item.getItemDescription())
                .isAvailable(item.isAvailable())
//                .numberOfUses()
                .build();
    }

    public static Item mapToItem(ItemDto itemDto) {
        return Item.builder()
                .itemId(itemDto.getItemId())
                .itemName(itemDto.getItemName())
                .itemDescription(itemDto.getItemDescription())
                .isAvailable(itemDto.isAvailable())
                .build();
    }
}
