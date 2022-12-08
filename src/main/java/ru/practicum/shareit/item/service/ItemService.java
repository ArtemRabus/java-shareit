package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    List<Item> getAll(int userId);

    ItemDto getById(int id);

    ItemDto save(ItemDto itemDto, int userId);

    ItemDto edit(ItemDto item, int userId, int id);

    void delete(int id);

    List<Item> search(String text, int userId);
}
