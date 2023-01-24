package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@WebMvcTest(UserService.class)
@AutoConfigureMockMvc
class UserServiceTest {
    @MockBean
    UserServiceImpl userService;
    UserRepository userRepository;
    UserDto userDto;
    User user;

    @BeforeEach
    void init() {
        userDto = new UserDto(1, "user", "user@email");
        user = new User(userDto.getId(), userDto.getName(), userDto.getEmail());
        userRepository = mock(UserRepository.class);
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    void getAllTest() {
        when(userRepository.findAll())
                .thenReturn(List.of(user));

        final List<UserDto> users = userService.getAll();

        assertNotNull(users);
        assertEquals(1, users.size());
    }

    @Test
    void getByIdTest() {
        when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));

        UserDto res = userService.getById(user.getId());

        assertNotNull(res);
        assertEquals(user.getId(), res.getId());
    }

    @Test
    void createTest() {
        when(userRepository.save(any()))
                .thenReturn(user);

        UserDto res = userService.create(userDto);

        assertNotNull(res);
        assertEquals(user.getId(), res.getId());
    }

    @Test
    void editTest() {
        User userUpdate = new User(1, "nameUp", "up@email");
        UserDto userDtoUp = new UserDto(userUpdate.getId(), userUpdate.getName(), userUpdate.getEmail());
        when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));
        when(userRepository.save(any()))
                .thenReturn(userUpdate);

        UserDto res = userService.edit(userDtoUp, user.getId());

        assertEquals(userUpdate.getName(), res.getName());
        assertEquals(userUpdate.getEmail(), res.getEmail());
    }

    @Test
    void editNullNameAndEmailTest() {
        User userUpdate = new User(1, user.getName(), user.getEmail());
        UserDto userDtoUp = new UserDto(userUpdate.getId(), null, null);
        when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));
        when(userRepository.save(any()))
                .thenReturn(userUpdate);

        UserDto res = userService.edit(userDtoUp, user.getId());

        assertEquals(user.getName(), res.getName());
        assertEquals(user.getEmail(), res.getEmail());
    }

    @Test
    void deleteTest() {
        when(userRepository.findById(anyInt()))
                .thenReturn(Optional.of(user));

        userService.delete(userDto.getId());
        List<User> users = userRepository.findAll();

        assertEquals(0, users.size());
    }
}