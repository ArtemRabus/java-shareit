package ru.practicum.shareit.item.comment;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommentDto {
    private int id;
    private String text;
    private int itemId;
    private String authorName;
    private LocalDateTime created;
}