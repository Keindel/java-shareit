package ru.practicum.shareit.item.comment;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CommentDto {
    @EqualsAndHashCode.Include
    private long id;
    @NotBlank
    @Size(max = 500)
    private String text;
    private String authorName;

    private String created;
}
