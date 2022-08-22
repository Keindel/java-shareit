package ru.practicum.shareit.item.comment;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CommentDto {
    @EqualsAndHashCode.Include
    private long id;
    private String text;
    private String authorName;

    private String created;
}
