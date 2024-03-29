package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.Create;
import ru.practicum.shareit.item.Update;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoInfo;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;


@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemService itemService;
    private static final String X_SHARER_USER_ID = "X-Sharer-User-Id";

    @GetMapping
    public List<ItemDtoInfo> getAll(@RequestHeader(X_SHARER_USER_ID) int userId,
                                    @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                    @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("getAll() in ItemController");
        return itemService.getAll(userId, from, size);
    }

    @GetMapping("/{itemId}")
    public ItemDtoInfo getById(@RequestHeader(X_SHARER_USER_ID) int userId, @PathVariable int itemId) {
        log.info("getById() in ItemController");
        return itemService.getById(itemId, userId);
    }

    @PostMapping
    public ItemDto create(@Validated({Create.class}) @RequestBody ItemDto itemDto,
                          @RequestHeader(X_SHARER_USER_ID) int userId) {
        log.info("save() in ItemController");
        return itemService.create(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@Validated({Update.class}) @RequestBody ItemDto itemDto,
                          @RequestHeader(X_SHARER_USER_ID) int userId, @PathVariable int itemId) {
        log.info("edit() in ItemController");
        return itemService.edit(itemDto, userId, itemId);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<HttpStatus> deleteItem(@PathVariable int itemId) {
        log.info("delete() in ItemController");
        itemService.delete(itemId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestParam String text, @RequestHeader(X_SHARER_USER_ID) int userId,
                                    @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                    @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("search() in ItemController");
        return itemService.search(text, userId, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader(X_SHARER_USER_ID) int userId, @PathVariable int itemId,
                                 @Valid @RequestBody CommentDto comment) {
        log.info("addComment() in ItemController");
        return itemService.addComment(userId, itemId, comment);
    }
}
