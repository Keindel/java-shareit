package ru.practicum.shareit.comment;

import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Size(max = 500)
    @Column(name = "comment_text")
    private String text;
    @NonNull
    @Column(name = "item_id")
    private Long itemId;
    @NonNull
    @Column(name = "author_id")
    private Long authorId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Comment comment = (Comment) o;
        return id != null && Objects.equals(id, comment.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
