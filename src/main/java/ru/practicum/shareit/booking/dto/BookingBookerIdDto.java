package ru.practicum.shareit.booking.dto;

import lombok.*;

@Data
@EqualsAndHashCode
public class BookingBookerIdDto {
    private final Long id;
    private final Long bookerId;
}
