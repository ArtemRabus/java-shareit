package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoOutput;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto create(int userId, ItemRequestDto itemRequestDto);

    List<ItemRequestDtoOutput> getAll(int userId);

    List<ItemRequestDtoOutput> getAllOtherUser(int userId, int from, int size);

    ItemRequestDtoOutput getById(int userId, int requestId);
}
