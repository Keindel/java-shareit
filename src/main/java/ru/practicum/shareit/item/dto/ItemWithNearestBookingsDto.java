package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.comment.Comment;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ItemWithNearestBookingsDto {
    @EqualsAndHashCode.Include
    private long id;
    private String name;
    private String description;
    private Boolean available;

    private Booking lastBooking;
    private Booking nextBooking;

    private List<Comment> comments;
}
