package ru.practicum.shareit.user.mapper;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

public class UserMapper {
     public static User toUserDto(UserDto userDto) { //так и не разобрался как в @Mapper работать c модификатором "static"
          return new User(userDto.getId(),
                  userDto.getName(),
                  userDto.getEmail());
     }
}
