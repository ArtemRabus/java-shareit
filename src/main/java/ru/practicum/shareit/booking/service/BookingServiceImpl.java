package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidateException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public BookingDto getById(int bookingId, int ownerId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(String.format("Request with id = %s not found", bookingId)));
        Item item = itemRepository.findById(booking.getItem().getId()).get();
        if (booking.getBooker().getId() == ownerId || item.getOwnerId() == ownerId) {
            log.info("Request found with id = {} (GetById())", booking.getId());
            return BookingMapper.toBookingDto(booking);
        }
        throw new NotFoundException("Only the author of the booking or the owner of the item can get the booking information");
    }

    @Override
    @Transactional
    public BookingDto save(BookingDtoIn bookingDtoIn, int ownerId) {
        User booker = userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id = %s not found", ownerId)));
        Item item = itemRepository.findById(bookingDtoIn.getItemId())
                .orElseThrow(() -> new NotFoundException(String.format("Item with id = %s not found", bookingDtoIn.getItemId())));
        BookingDto bookingDto = BookingMapper.toBookingDto(bookingDtoIn, item);
        validateBooking(bookingDto);
        bookingDto.setItem(item);
        bookingDto.setBooker(booker);
        if (booker.getId() == item.getOwnerId()) {
            throw new NotFoundException(String.format("You are the owner of the item with id = %s", item.getId()));
        }
        if (item.getAvailable()) {
            Booking booking = BookingMapper.toBooking(bookingDto, item, booker);
            booking.setStatus(Status.WAITING);
            log.info("Request with id = {} saved (save())", booking.getId());
            return BookingMapper.toBookingDto(bookingRepository.save(booking));
        }
        throw new ValidateException(String.format("Item with id = %s is not available for rent", bookingDto.getItem().getId()));
    }

    @Override
    @Transactional
    public BookingDto confirmation(int bookingId, int ownerId, boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(String.format("Request with id = %s not found", bookingId)));
        userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id = %s not found", ownerId)));
        itemRepository.findById(booking.getItem().getId())
                .orElseThrow(() -> new NotFoundException(String.format("Item with id = %s not found", booking.getItem().getId())));

        if (booking.getItem().getOwnerId() == ownerId) {
            if (booking.getStatus().equals(Status.WAITING)) {
                booking.setStatus(approved ? Status.APPROVED : Status.REJECTED);
            } else {
                throw new ValidateException("Changing the booking status is not available");
            }
            log.info("The booking status of the request with id = {} has been changed to {} (confirmation())", booking.getId(), booking.getStatus());
            return BookingMapper.toBookingDto(bookingRepository.save(booking));
        }
        throw new NotFoundException("Only the owner of the item can edit the booking status");
    }

    @Override
    public List<BookingDto> getAllByBookerId(int bookerId, BookingState state) {
        userRepository.findById(bookerId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id = %s not found", bookerId)));
        Set<Booking> bookings = new HashSet<>(bookingRepository.findAllByBookerId(bookerId));
        if (bookings.isEmpty()) {
            throw new NotFoundException("No bookings found");
        } else {
            log.info("All bookings of the user with id = {} (getAllByBookerId()) have been received", bookerId);
            return filterByState(bookings, state);
        }
    }

    @Override
    public List<BookingDto> getAllByOwnerId(int ownerId, BookingState state) {
        userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id = %s not found", ownerId)));
        Set<Booking> bookings = new HashSet<>(bookingRepository.findAllByOwnerId(ownerId));
        if (bookings.isEmpty()) {
            throw new NotFoundException("No bookings found");
        } else {
            log.info("All bookings of the user with id = {} have been received (get all by owner id())", ownerId);
            return filterByState(bookings, state);
        }
    }

    private List<BookingDto> filterByState(Set<Booking> bookings, BookingState state) {
        List<BookingDto> bookingList = null;
        switch (state) {
            case ALL:
                bookingList = bookings.stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
                break;
            case WAITING:
                bookingList = bookings.stream()
                        .filter(booking -> booking.getStatus().equals(Status.WAITING))
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
                break;
            case REJECTED:
                bookingList = bookings.stream()
                        .filter(booking -> booking.getStatus().equals(Status.REJECTED))
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
                break;
            case PAST:
                bookingList = bookings.stream()
                        .filter(booking -> booking.getEnd().isBefore(LocalDateTime.now()))
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
                break;
            case FUTURE:
                bookingList = bookings.stream()
                        .filter(booking -> booking.getStart().isAfter(LocalDateTime.now()))
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
                break;
            case CURRENT:
                bookingList = bookings.stream()
                        .filter(booking -> booking.getStart().isBefore(LocalDateTime.now()) &&
                                booking.getEnd().isAfter(LocalDateTime.now()))
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
                break;
        }
        return bookingList;
    }

    private void validateBooking(BookingDto bookingDto) {
        if (bookingDto.getStart().isBefore(LocalDateTime.now()) || bookingDto.getStart() == null) {
            throw new ValidateException(String.format("The booking start date is not specified or is in the past"));
        } else if (bookingDto.getEnd().isBefore(LocalDateTime.now()) || bookingDto.getEnd() == null) {
            throw new ValidateException(String.format("The end date of the reservation is not specified or is in the past"));
        } else if (bookingDto.getEnd().isBefore(bookingDto.getStart())) {
            throw new ValidateException(String.format("The end date of the reservation is earlier than the start date"));
        }
    }

//    private void validState(String state) {
//        try {
//            BookingState.valueOf(state);
//        } catch (IllegalArgumentException e) {
//            throw new MessageFailedException(String.format("Unknown state: %s", state));
//        }
//    }
}
