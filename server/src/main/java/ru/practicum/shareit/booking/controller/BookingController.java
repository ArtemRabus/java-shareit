package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/bookings")
public class BookingController {
    private final BookingService bookingService;
    private static final String X_SHARER_USER_ID = "X-Sharer-User-Id";

    @GetMapping("{bookingId}")
    public BookingDto getById(@RequestHeader(X_SHARER_USER_ID) int ownerId,
                              @PathVariable int bookingId) {
        log.info("getById() in BookingController");
        return bookingService.getById(bookingId, ownerId);
    }

    @GetMapping
    public List<BookingDto> getAllByBookerId(@RequestHeader(X_SHARER_USER_ID) int bookerId,
                                             @RequestParam(defaultValue = "ALL") String state,
                                             @RequestParam(defaultValue = "0") int from,
                                             @RequestParam(defaultValue = "10") int size) {
        log.info("getAllByBookerId() in BookingController");
        return bookingService.getAllByBookerId(bookerId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllByOwnerId(@RequestHeader(X_SHARER_USER_ID) int ownerId,
                                            @RequestParam(defaultValue = "ALL") String state,
                                            @RequestParam(defaultValue = "0") int from,
                                            @RequestParam(defaultValue = "10") int size) {
        log.info("getAllByOwnerId() in BookingController");
        return bookingService.getAllByOwnerId(ownerId, state, from, size);
    }

    @PostMapping
    public BookingDto create(@RequestHeader(X_SHARER_USER_ID) int userId,
                             @RequestBody BookingDtoRequest bookingDto) {
        log.info("save() in BookingController");
        return bookingService.save(userId, bookingDto);
    }

    @PatchMapping("{bookingId}")
    public BookingDto confirmation(@RequestHeader(X_SHARER_USER_ID) int userId,
                                   @PathVariable int bookingId,
                                   @RequestParam boolean approved) {
        log.info("confirmation() in BookingController");
        return bookingService.confirmation(bookingId, userId, approved);
    }

}
