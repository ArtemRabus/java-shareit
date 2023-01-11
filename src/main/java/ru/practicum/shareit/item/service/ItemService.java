package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoInfo;

import java.util.List;

public interface ItemService {
    List<ItemDtoInfo> getAll(int userId, int from, int size);

    ItemDtoInfo getById(int itemId, int ownerId);

    ItemDto create(ItemDto itemDto, int userId);

    ItemDto edit(ItemDto item, int userId, int id);

    void delete(int id);

    List<ItemDto> search(String text, int userId, int from, int size);

    CommentDto addComment(int userId, int itemId, CommentDto commentDto);
}
