package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@EqualsAndHashCode
public class BookingInputDto {
    private Long id;
    @NotNull
    @FutureOrPresent
    @Column(name = "start_date")
    private LocalDateTime start;
    @NotNull
    @FutureOrPresent
    @Column(name = "end_date")
    private LocalDateTime end;
    @NotNull
    private Long itemId;
}