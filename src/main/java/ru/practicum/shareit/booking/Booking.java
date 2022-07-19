package ru.practicum.shareit.booking;


import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.lang.NonNull;

import java.time.LocalDate;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Booking {
    @EqualsAndHashCode.Include
    private long bookingId;
    @NonNull
    private LocalDate bookingStart;
    @NonNull
    private LocalDate bookingEnd;
    private long itemId;
    private long userId;
    // TODO OwnerReaction.class for rejection/awaiting/approve?
    private boolean approvedByOwner;
}

