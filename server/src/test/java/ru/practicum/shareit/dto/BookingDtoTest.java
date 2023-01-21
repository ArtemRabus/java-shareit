package ru.practicum.shareit.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingDtoTest {
    @Autowired
    private JacksonTester<BookingDto> json;

    @Test
    void serializeTest() throws IOException {
        User user = new User(1, "user", "us@email");
        Item item = new Item(1, "item", "descrItem", true, user.getId(), null);
        BookingDto dto = new BookingDto(1, item, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(3),
                user, Status.APPROVED);

        JsonContent<BookingDto> res = json.write(dto);

        assertThat(res).hasJsonPath("$.id");
        assertThat(res).extractingJsonPathNumberValue("$.id").isEqualTo(dto.getId());
        assertThat(res).extractingJsonPathNumberValue("$.item.id").isEqualTo(dto.getItem().getId());
    }

}