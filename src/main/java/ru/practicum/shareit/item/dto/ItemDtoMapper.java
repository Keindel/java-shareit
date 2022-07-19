package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;


public class ItemDtoMapper {
    public static ItemDto mapToDto(Item item) {
        return ItemDto.builder()
                .itemId(item.getItemId())
                .itemName(item.getItemName())
                .itemDescription(item.getItemDescription())
//                .numberOfUses()
                .build();
    }

    public static Item mapToItem(ItemDto itemDto) {
        return null;
    }
}
