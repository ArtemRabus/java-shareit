package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.Create;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.Update;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
@Validated
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ItemController {
    ItemClient itemClient;
    static final String X_SHARER_USER_ID = "X-Sharer-User-Id";

    @GetMapping
    public ResponseEntity<Object> getAll(@RequestHeader(X_SHARER_USER_ID) int userId,
                                         @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                         @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("Вызван метод getAll() в ItemController");
        return itemClient.getAll(userId, from, size);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getById(@RequestHeader(X_SHARER_USER_ID) int userId,
                                          @PathVariable int itemId) {
        log.info("getById() in ItemController");
        return itemClient.getById(userId, itemId);
    }

    @PostMapping
    public ResponseEntity<Object> create(@Validated(Create.class) @RequestBody ItemDto itemDto,
                                         @RequestHeader(X_SHARER_USER_ID) int userId) {
        log.info("save() in ItemController");
        return itemClient.create(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@Validated(Update.class) @RequestBody ItemDto itemDto,
                                         @RequestHeader(X_SHARER_USER_ID) int userId, @PathVariable int itemId) {
        log.info("edit() in ItemController");
        return itemClient.edit(itemDto, userId, itemId);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Object> deleteItem(@PathVariable int itemId) {
        log.info("delete() in ItemController");
        return itemClient.delete(itemId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItem(@RequestParam String text, @RequestHeader(X_SHARER_USER_ID) int userId,
                                             @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                             @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("search() in ItemController");
        return itemClient.search(text, userId, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader(X_SHARER_USER_ID) int userId, @PathVariable int itemId,
                                             @Valid @NotNull @RequestBody CommentDto comment) {
        log.info("addComment() in ItemController");
        return itemClient.addComment(userId, itemId, comment);
    }
}
