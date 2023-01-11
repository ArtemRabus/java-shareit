package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoOutput;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
@Slf4j
@Validated
public class ItemRequestController {
    private final ItemRequestService itemRequestService;
    private static final String X_SHARER_USER_ID = "X-Sharer-User-Id";

    @PostMapping
    public ItemRequestDto create(@RequestHeader(X_SHARER_USER_ID) int userId, @Valid @RequestBody ItemRequestDto itemRequestDto) {
        log.info("create() in ItemRequestController");
        return itemRequestService.create(userId, itemRequestDto);
    }

    @GetMapping
    public List<ItemRequestDtoOutput> getAllByUser(@RequestHeader(X_SHARER_USER_ID) int userId) {
        log.info("getAllByUser() in ItemRequestController");
        return itemRequestService.getAll(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDtoOutput> getAllOtherUser(@RequestHeader(X_SHARER_USER_ID) int userId,
                                                      @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                      @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("getAllOtherUser() in ItemRequestController");
        return itemRequestService.getAllOtherUser(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDtoOutput getById(@RequestHeader(X_SHARER_USER_ID) int userId, @PathVariable int requestId) {
        log.info("getById() in ItemRequestController");
        return itemRequestService.getById(userId, requestId);
    }
}
