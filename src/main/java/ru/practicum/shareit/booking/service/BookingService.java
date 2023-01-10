package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;

import java.util.List;

public interface BookingService {

    BookingDto getById(int id, int ownerId);

    BookingDto confirmation(int bookingId, int ownerId, boolean approved);

    BookingDto save(int userId, BookingDtoRequest bookingDto);

    List<BookingDto> getAllByBookerId(int bookerId, String state, int from, int size);

    List<BookingDto> getAllByOwnerId(int ownerId, String state, int from, int size);

}
