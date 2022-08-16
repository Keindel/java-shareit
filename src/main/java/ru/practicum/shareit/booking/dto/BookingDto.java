package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.shareit.booking.Status;

import java.time.LocalDateTime;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class BookingDto {
    private Long id;
    private String start;
    private String end;
    private Long itemId;
    private Long bookerId;
    private Status status;
}
