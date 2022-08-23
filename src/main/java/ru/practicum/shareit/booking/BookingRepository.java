package ru.practicum.shareit.booking;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Collection;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Collection<Booking> findAllByBookerId(long bookerId, Sort sort);

    Collection<Booking> findAllByBookerIdAndEndIsBefore(long bookerId, LocalDateTime end, Sort sort);

    Collection<Booking> findAllByBookerIdAndStartIsBeforeAndEndIsAfter(long bookerId,
                                                                       LocalDateTime start,
                                                                       LocalDateTime end,
                                                                       Sort sort);

    Collection<Booking> findAllByBookerIdAndStartIsAfter(long bookerId,
                                                         LocalDateTime start,
                                                         Sort sort);

    Collection<Booking> findAllByBookerIdAndStatusEquals(long bookerId,
                                                         Status status,
                                                         Sort sort);

    Collection<Booking> findAllByItemOwnerId(long ownerId, Sort sort);

    Collection<Booking> findAllByItemOwnerIdAndEndIsBefore(long ownerId, LocalDateTime end, Sort sort);

    Collection<Booking> findAllByItemOwnerIdAndStartIsBeforeAndEndIsAfter(long ownerId,
                                                                          LocalDateTime start,
                                                                          LocalDateTime end,
                                                                          Sort sort);

    Collection<Booking> findAllByItemOwnerIdAndStartIsAfter(long ownerId,
                                                            LocalDateTime start,
                                                            Sort sort);

    Collection<Booking> findAllByItemOwnerIdAndStatusEquals(long ownerId,
                                                            Status status,
                                                            Sort sort);

    Booking getFirstByItemIdAndStartIsBeforeOrderByStartDesc(long itemId, LocalDateTime start);
    Booking getFirstByItemIdAndStartIsAfterOrderByStartAsc(long itemId, LocalDateTime start);
}
