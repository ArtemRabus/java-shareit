package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.Create;
import ru.practicum.shareit.item.Update;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemService itemService;
    private static final String X_SHARER_USER_ID = "X-Sharer-User-Id";

    @GetMapping
    public List<Item> getAll(@RequestHeader(X_SHARER_USER_ID) int userId) {
        log.info("getAll() in ItemController");
        return itemService.getAll(userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getById(@PathVariable int itemId) {
        log.info("getById() in ItemController");
        return itemService.getById(itemId);
    }

    @PostMapping
    public ItemDto create(@Validated({Create.class}) @RequestBody ItemDto itemDto,
                          @RequestHeader(X_SHARER_USER_ID) int userId) {
        log.info("save() in ItemController");
        return itemService.save(itemDto, userId);
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
    public List<Item> searchItem(@RequestParam String text, @RequestHeader(X_SHARER_USER_ID) int userId) {
        log.info("search() in ItemController");
        return itemService.search(text, userId);
    }
}
