package ru.practicum.shareit.booking;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

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

//    @Query("SELECT boo FROM Booking boo " +
//            "JOIN Item on boo.itemId=Item.id " +
//            "WHERE Item.ownerId = :ownerId " +
//            "ORDER BY boo.start DESC")
//    Collection<Booking> findAllByOwnerId(long ownerId);
//
//    @Query("SELECT boo FROM Booking boo " +
//            "JOIN Item on boo.itemId=Item.id " +
//            "WHERE Item.ownerId = :ownerId " +
//            "AND boo.end < :endTime " +
//            "ORDER BY boo.start DESC")
//    Collection<Booking> findAllByOwnerIdAndEndIsBefore(long ownerId, LocalDateTime endTime);
//
//    @Query("SELECT boo FROM Booking boo " +
//            "JOIN Item on boo.itemId=Item.id " +
//            "WHERE Item.ownerId = :ownerId " +
//            "AND boo.start < :now " +
//            "AND boo.end > :now " +
//            "ORDER BY boo.start DESC")
//    Collection<Booking> findAllByOwnerIdAndStartIsBeforeAndEndIsAfter(long ownerId,
//                                                                      LocalDateTime now);
//
//    @Query("SELECT boo FROM Booking boo " +
//            "JOIN Item on boo.itemId=Item.id " +
//            "WHERE Item.ownerId = :ownerId " +
//            "AND boo.end < :end " +
//            "ORDER BY boo.start DESC")
//    Collection<Booking> findAllByOwnerIdAndStartIsAfter(long ownerId);
//
//    @Query("SELECT boo FROM Booking boo " +
//            "JOIN Item on boo.itemId=Item.id " +
//            "WHERE Item.ownerId = :ownerId " +
//            "AND boo.end < :end " +
//            "ORDER BY boo.start DESC")
//    Collection<Booking> findAllByOwnerIdAndStatusEquals(long ownerId);
}
