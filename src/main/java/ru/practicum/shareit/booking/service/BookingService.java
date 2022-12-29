package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.List;

public interface BookingService {

    BookingDto getById(int id, int ownerId);

    BookingDto confirmation(int bookingId, int ownerId, boolean approved);

    BookingDto save(BookingDtoIn bookingDto, int userId);

    List<BookingDto> getAllByBookerId(int bookerId, BookingState state);

    List<BookingDto> getAllByOwnerId(int ownerId, BookingState state);

}
