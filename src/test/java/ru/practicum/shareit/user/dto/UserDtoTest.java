package ru.practicum.shareit.user.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class UserDtoTest {
    @Autowired
    private JacksonTester<UserDto> json;

    @Test
    void serializeTest() throws IOException {
        UserDto userDto = new UserDto(1, "userName", "user@mail");
        JsonContent<UserDto> res = json.write(userDto);
        assertThat(res).hasJsonPath("$.id");
        assertThat(res).extractingJsonPathNumberValue("$.id").isEqualTo(userDto.getId());
    }
}

