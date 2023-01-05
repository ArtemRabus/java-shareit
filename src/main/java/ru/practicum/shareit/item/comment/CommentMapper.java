package ru.practicum.shareit.item.comment;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Component
public class CommentMapper {
    public static Comment toComment(CommentDto commentDto, Item item, User author, LocalDateTime dateTime) {
        return new Comment(commentDto.getId(),
                commentDto.getText(),
                item,
                author,
                dateTime);
    }

    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(comment.getId(),
                comment.getText(),
                comment.getItem().getId(),
                comment.getAuthor().getName(),
                comment.getCreated());
    }
}
