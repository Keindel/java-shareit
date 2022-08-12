package ru.practicum.shareit.item;

import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Size(max = 50)
    @Column(name = "item_name")
    private String name;
    @NonNull
    @Size(max = 500)
    private String description;
    @NonNull
    @Column(name = "owner_id")
    private Long ownerId;
    @NonNull
    private Boolean available;

    @Column(name = "request_id")
    private Long requestId;

    @ElementCollection
    @CollectionTable(name = "bookings", joinColumns = @JoinColumn(name = "item_id"))
    @Column(name = "id")
    private List<Long> bookingsIds;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Item item = (Item) o;
        return id != null && Objects.equals(id, item.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
