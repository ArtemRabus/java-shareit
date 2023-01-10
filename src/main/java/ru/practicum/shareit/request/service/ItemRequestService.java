package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto create(int userId, ItemRequestDto itemRequestDto);

    List<ItemRequestDtoOut> getAll(int userId);

    List<ItemRequestDtoOut> getAllOtherUser(int userId, int from, int size);

    ItemRequestDtoOut getById(int userId, int requestId);
}
