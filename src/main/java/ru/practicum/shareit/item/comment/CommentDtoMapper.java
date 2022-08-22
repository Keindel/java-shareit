package ru.practicum.shareit.item.comment;

import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class CommentDtoMapper {
    static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
    public CommentDto toDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .authorName(comment.getAuthor().getName())
                .text(comment.getText())
                .created(formatter.format(comment.getCreated()))
                .build();
    }

    public Comment toComment(CommentDto commentDto) {
        Comment comment = new Comment();
        comment.setText(commentDto.getText());
        return comment;
    }
}
