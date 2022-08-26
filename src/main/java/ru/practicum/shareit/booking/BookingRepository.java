package ru.practicum.shareit.booking;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Collection;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Collection<Booking> findAllByBookerId(long bookerId, Pageable page);

    Collection<Booking> findAllByBookerIdAndEndIsBefore(long bookerId, LocalDateTime end, Pageable page);

    Collection<Booking> findAllByBookerIdAndStartIsBeforeAndEndIsAfter(long bookerId,
                                                                       LocalDateTime start,
                                                                       LocalDateTime end,
                                                                       Pageable page);

    Collection<Booking> findAllByBookerIdAndStartIsAfter(long bookerId,
                                                         LocalDateTime start,
                                                         Pageable page);

    Collection<Booking> findAllByBookerIdAndStatusEquals(long bookerId,
                                                         Status status,
                                                         Pageable page);

    Collection<Booking> findAllByItemOwnerId(long ownerId, Pageable page);

    Collection<Booking> findAllByItemOwnerIdAndEndIsBefore(long ownerId, LocalDateTime end, Pageable page);

    Collection<Booking> findAllByItemOwnerIdAndStartIsBeforeAndEndIsAfter(long ownerId,
                                                                          LocalDateTime start,
                                                                          LocalDateTime end,
                                                                          Pageable page);

    Collection<Booking> findAllByItemOwnerIdAndStartIsAfter(long ownerId,
                                                            LocalDateTime start,
                                                            Pageable page);

    Collection<Booking> findAllByItemOwnerIdAndStatusEquals(long ownerId,
                                                            Status status,
                                                            Pageable page);

    Booking getFirstByItemIdAndStartIsBeforeOrderByStartDesc(long itemId, LocalDateTime start);

    Booking getFirstByItemIdAndStartIsAfterOrderByStartAsc(long itemId, LocalDateTime start);
}
