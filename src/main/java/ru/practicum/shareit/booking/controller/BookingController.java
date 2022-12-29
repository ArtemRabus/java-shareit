package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.MessageFailedException;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
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
                                             @RequestParam(defaultValue = "ALL") String state) {
        BookingState state1;
        try {
            state1 = BookingState.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new MessageFailedException(String.format("Unknown state: %s", state));
        }
        log.info("getAllByBookerId() in BookingController");
        return bookingService.getAllByBookerId(bookerId, state1);
    }

    @GetMapping("/owner")
    public List<BookingDto> getBookingByUser(@RequestHeader(X_SHARER_USER_ID) int ownerId,
                                             @RequestParam(defaultValue = "ALL") String state) {
        BookingState state1;
        try {
            state1 = BookingState.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new MessageFailedException(String.format("Unknown state: %s", state));
        }
        log.info("getBookingByUser() in BookingController");
        return bookingService.getAllByOwnerId(ownerId, state1);
    }

    @PostMapping
    public BookingDto create(@RequestHeader(X_SHARER_USER_ID) int userId,
                             @Valid @RequestBody BookingDtoIn bookingDto) {
        log.info("save() in BookingController");
        return bookingService.save(bookingDto, userId);
    }

    @PatchMapping("{bookingId}")
    public BookingDto confirmation(@RequestHeader(X_SHARER_USER_ID) int userId,
                                   @PathVariable int bookingId,
                                   @RequestParam boolean approved) {
        log.info("confirmation() in BookingController");
        return bookingService.confirmation(bookingId, userId, approved);
    }

}
