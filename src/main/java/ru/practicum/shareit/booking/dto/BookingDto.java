package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class BookingDto {
    private Long id;
    private String start;
    private String end;
    private Item item;
    private User booker;
    private Status status;
}
