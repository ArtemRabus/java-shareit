package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemRequestDtoTest {
    @Autowired
    private JacksonTester<ItemRequestDto> json;

    @Test
    void serializeTest() throws IOException {
        ItemRequestDto dto = new ItemRequestDto(1, "desc", LocalDateTime.now());
        JsonContent<ItemRequestDto> res = json.write(dto);
        assertThat(res).hasJsonPath("$.id");
        assertThat(res).extractingJsonPathNumberValue("$.id").isEqualTo(dto.getId());
    }
}

