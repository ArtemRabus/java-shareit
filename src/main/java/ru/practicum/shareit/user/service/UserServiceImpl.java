package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public List<User> getAll() {
        return userRepository.getAll();
    }

    public User getById(int id) {
        return userRepository.getById(id)
                .orElseThrow(() -> new NotFoundException(String.format("User with id = %s not found", id)));
    }

    public User save(UserDto userDto) {
        User user = UserMapper.toUserDto(userDto);
        checkEmail(user);
        return userRepository.save(user);
    }

    public User edit(UserDto userDto, int id) {
        User newUser = UserMapper.toUserDto(userDto);
        checkEmail(newUser);
        User oldUser = getById(id);
        return userRepository.edit(newUser, oldUser);
    }

    public void delete(int id) {
        User user = getById(id);
        userRepository.delete(user);
    }

    public void checkEmail(User user) {
        if (userRepository.contains(user.getEmail())) {
            throw new RuntimeException("A user with this email already exists");
        }
    }
}