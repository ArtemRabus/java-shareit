package ru.practicum.shareit.request;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
@Slf4j
@Validated
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ItemRequestController {
    RequestClient requestClient;
    private static final String X_SHARER_USER_ID = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(X_SHARER_USER_ID) int userId,
                                         @Valid @NotNull @RequestBody ItemRequestDto itemRequestDto) {
        log.info("create() in ItemRequestController");
        return requestClient.create(userId, itemRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getAllByUser(@RequestHeader(X_SHARER_USER_ID) int userId) {
        log.info("getAllByUser() in ItemRequestController");
        return requestClient.getAllByUser(userId);
    }


    @GetMapping("/all")
    public ResponseEntity<Object> getAllOtherUser(@RequestHeader(X_SHARER_USER_ID) int userId,
                                                  @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                  @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("getAllOtherUser() in ItemRequestController");
        return requestClient.getAllOtherUser(userId, from, size);
    }


    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getById(@RequestHeader(X_SHARER_USER_ID) int userId, @PathVariable int requestId) {
        log.info("getById() in ItemRequestController");
        return requestClient.getById(userId, requestId);
    }
}
