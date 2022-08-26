package ru.practicum.shareit.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Collection;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Page<Booking> findAllByBookerId(long bookerId, Pageable page);

    Page<Booking> findAllByBookerIdAndEndIsBefore(long bookerId, LocalDateTime end, Pageable page);

    Page<Booking> findAllByBookerIdAndStartIsBeforeAndEndIsAfter(long bookerId,
                                                                       LocalDateTime start,
                                                                       LocalDateTime end,
                                                                       Pageable page);

    Page<Booking> findAllByBookerIdAndStartIsAfter(long bookerId,
                                                         LocalDateTime start,
                                                         Pageable page);

    Page<Booking> findAllByBookerIdAndStatusEquals(long bookerId,
                                                         Status status,
                                                         Pageable page);

    Page<Booking> findAllByItemOwnerId(long ownerId, Pageable page);

    Page<Booking> findAllByItemOwnerIdAndEndIsBefore(long ownerId, LocalDateTime end, Pageable page);

    Page<Booking> findAllByItemOwnerIdAndStartIsBeforeAndEndIsAfter(long ownerId,
                                                                          LocalDateTime start,
                                                                          LocalDateTime end,
                                                                          Pageable page);

    Page<Booking> findAllByItemOwnerIdAndStartIsAfter(long ownerId,
                                                            LocalDateTime start,
                                                            Pageable page);

    Page<Booking> findAllByItemOwnerIdAndStatusEquals(long ownerId,
                                                            Status status,
                                                            Pageable page);

    Booking getFirstByItemIdAndStartIsBeforeOrderByStartDesc(long itemId, LocalDateTime start);

    Booking getFirstByItemIdAndStartIsAfterOrderByStartAsc(long itemId, LocalDateTime start);
}
