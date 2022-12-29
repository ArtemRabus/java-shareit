package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoForItem;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;


public class BookingMapper {
    public static BookingDto toBookingDto(Booking booking) {
        return new BookingDto(booking.getId(),
                booking.getItem(),
                booking.getStart(),
                booking.getEnd(),
                booking.getBooker(),
                booking.getStatus()
        );
    }

    public static Booking toBooking(BookingDto bookingDto, Item item, User booker) {
        return new Booking(bookingDto.getStart(),
                bookingDto.getEnd(),
                item,
                booker);
    }

    public static BookingDto toBookingDto(BookingDtoIn bookingDtoIn, Item item) {
        return new BookingDto(item,
                bookingDtoIn.getStart(),
                bookingDtoIn.getEnd());
    }

    public static BookingDtoForItem toBookingDtoForItem(Booking booking) {
        return new BookingDtoForItem(booking.getId(),
                booking.getBooker().getId());
    }
}