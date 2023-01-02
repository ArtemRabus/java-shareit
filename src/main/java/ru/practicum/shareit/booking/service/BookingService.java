package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoIn;

import java.util.List;

public interface BookingService {

    BookingDto getById(int id, int ownerId);

    BookingDto confirmation(int bookingId, int ownerId, boolean approved);

    BookingDto save(BookingDtoIn bookingDto, int userId);

    List<BookingDto> getAllByBookerId(int bookerId, String state);

    List<BookingDto> getAllByOwnerId(int ownerId, String state);

}
