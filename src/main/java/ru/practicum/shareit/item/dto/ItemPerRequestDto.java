package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.shareit.item.comment.Comment;

import java.util.List;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ItemPerRequestDto {
    @EqualsAndHashCode.Include
    private long id;
    private String name;
    private String description;
    private Boolean available;

    private List<Long> bookingsIds;
    private List<Comment> comments;
    private Long requestId;
}
