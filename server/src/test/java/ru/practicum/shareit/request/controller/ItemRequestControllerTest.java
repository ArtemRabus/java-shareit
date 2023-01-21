package ru.practicum.shareit.request.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoOutput;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
@AutoConfigureMockMvc
class ItemRequestControllerTest {
    @MockBean
    ItemRequestService itemRequestService;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    ItemRequestDtoOutput itemRequestDtoOut;
    ItemRequestDto itemRequestDto;

    @BeforeEach
    void init() {
        itemRequestDtoOut = new ItemRequestDtoOutput(1, "descr", LocalDateTime.now(), null);
        itemRequestDto = new ItemRequestDto(itemRequestDtoOut.getId(), itemRequestDtoOut.getDescription(),
                itemRequestDtoOut.getCreated());
    }

    @Test
    void createTest() throws Exception {
        when(itemRequestService.create(anyInt(), any()))
                .thenReturn(itemRequestDto);

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", "1")
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDto.getId()), Integer.class))
                .andExpect(jsonPath("$.description", is(itemRequestDto.getDescription()), String.class));

        verify(itemRequestService, times(1))
                .create(anyInt(), any());
    }

    @Test
    void getAllByUserTest() throws Exception {
        when(itemRequestService.getAll(anyInt()))
                .thenReturn(List.of(itemRequestDtoOut));

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemRequestDto.getId()), Integer.class))
                .andExpect(jsonPath("$[0].description", is(itemRequestDto.getDescription()), String.class));

        verify(itemRequestService, times(1))
                .getAll(anyInt());
    }

    @Test
    void getAllOtherUserTest() throws Exception {
        when(itemRequestService.getAllOtherUser(anyInt(), anyInt(), anyInt()))
                .thenReturn(List.of(itemRequestDtoOut));

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", "1")
                        .param("from", "0")
                        .param("size", "2")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemRequestDto.getId()), Integer.class))
                .andExpect(jsonPath("$[0].description", is(itemRequestDto.getDescription()), String.class));

        verify(itemRequestService, times(1))
                .getAllOtherUser(anyInt(), anyInt(), anyInt());
    }

    @Test
    void getByIdTest() throws Exception {
        when(itemRequestService.getById(anyInt(), anyInt()))
                .thenReturn(itemRequestDtoOut);

        mockMvc.perform(get("/requests/{requestId}", itemRequestDto.getId())
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDto.getId()), Integer.class))
                .andExpect(jsonPath("$.description", is(itemRequestDto.getDescription()), String.class));

        verify(itemRequestService, times(1))
                .getById(anyInt(), anyInt());
    }
}