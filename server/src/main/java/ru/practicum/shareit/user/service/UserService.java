package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAll();

    UserDto getById(int id);

    UserDto create(UserDto user);

    UserDto edit(UserDto user, int id);

    void delete(int id);
}
