package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemDtoTest {

    @Autowired
    private JacksonTester<ItemDto> json;

    @Test
    void serializeTest() throws IOException {
        ItemDto dto = new ItemDto(1, "item", "descr", true, null);

        JsonContent<ItemDto> res = json.write(dto);

        assertThat(res).hasJsonPath("$.id");
        assertThat(res).extractingJsonPathNumberValue("$.id").isEqualTo(dto.getId());
    }
}

