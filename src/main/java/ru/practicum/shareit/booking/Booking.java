package ru.practicum.shareit.booking;


import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.lang.NonNull;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import javax.validation.constraints.Future;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bookings")

public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    @Future
    @Column(name = "start_date")
    private LocalDateTime start;
    @NonNull
    @Future
    @Column(name = "end_date")
    private LocalDateTime end;

    @NonNull
    @ManyToOne//(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    //@JoinColumn(name = "item_id")
    private Item item;
    @NonNull
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "booker_id")
    private User booker;
    @Enumerated(EnumType.STRING)
    private Status status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Booking booking = (Booking) o;
        return id != null && Objects.equals(id, booking.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

