package ru.practicum.shareit.user;

import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Column(name = "user_name")
    private String name;
    @NonNull
    @Email
    private String email;

    @ElementCollection
    @CollectionTable(name = "items", joinColumns = @JoinColumn(name = "owner_id"))
    @Column(name = "id")
    private List<Long> itemsIdsForSharing;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id) && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
