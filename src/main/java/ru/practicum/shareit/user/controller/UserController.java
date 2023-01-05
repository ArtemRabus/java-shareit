package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<User> getAll() {
        log.info("getAll() in UserController");
        return userService.getAll();
    }

    @GetMapping("/{userId}")
    public UserDto getById(@PathVariable int userId) {
        log.info("getById() in UserController");
        return userService.getById(userId);
    }

    @PostMapping
    public UserDto createUser(@Valid @RequestBody UserDto user) {
        log.info("save() in UserController");
        return userService.create(user);
    }

    @PatchMapping("/{userId}")
    public UserDto editUser(@RequestBody UserDto user, @PathVariable int userId) {
        log.info("edit() in UserController");
        return userService.edit(user, userId);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable int userId) {
        log.info("delete() in UserController");
        userService.delete(userId);
        return ResponseEntity.ok().build();
    }
}

