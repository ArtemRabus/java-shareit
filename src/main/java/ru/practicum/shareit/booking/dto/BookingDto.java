package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {
    private int id;
    private Item item;
    @FutureOrPresent
    private LocalDateTime start;
    @Future
    private LocalDateTime end;
    private User booker;
    private Status status;

    public BookingDto(Item item, LocalDateTime start, LocalDateTime end) {
        this.item = item;
        this.start = start;
        this.end = end;
    }
}
