package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc
class BookingControllerTest {
    @MockBean
    BookingService bookingService;
    @Autowired
    ObjectMapper mapper;
    @Autowired
    MockMvc mockMvc;
    User booker;
    User owner;
    Item item;
    BookingDto bookingDto;
    BookingDtoRequest bookingDtoIn;

    @BeforeEach
    void init() {
        booker = new User(1, "booker", "us@mail");
        owner = new User(2, "owner", "ow@email");
        item = new Item(1, "item", "descrItem", true, owner.getId(), null);
        bookingDto = new BookingDto(1, item, LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2), booker, Status.WAITING);
        bookingDtoIn = new BookingDtoRequest(bookingDto.getItem().getId(), bookingDto.getStart(), bookingDto.getEnd());
    }

    @Test
    void getByIdTest() throws Exception {
        when(bookingService.getById(anyInt(), anyInt()))
                .thenReturn(bookingDto);

        mockMvc.perform(get("/bookings/{bookingId}", bookingDto.getId())
                        .header("X-Sharer-User-Id", "1")
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Integer.class))
                .andExpect(jsonPath("$.booker", is(bookingDto.getBooker()), User.class))
                .andExpect(jsonPath("$.item", is(bookingDto.getItem()), Item.class));

        verify(bookingService, times(1))
                .getById(anyInt(), anyInt());
    }

    @Test
    void getAllByBookerIdTest() throws Exception {
        when(bookingService.getAllByBookerId(anyInt(), anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingDto));

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", booker.getId())
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "5")
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id", is(bookingDto.getId()), Integer.class))
                .andExpect(jsonPath("$[0].booker", is(bookingDto.getBooker()), User.class))
                .andExpect(jsonPath("$[0].item", is(bookingDto.getItem()), Item.class));

        verify(bookingService, times(1))
                .getAllByBookerId(anyInt(), anyString(), anyInt(), anyInt());
    }

    @Test
    void getAllByOwnerIdTest() throws Exception {
        when(bookingService.getAllByOwnerId(anyInt(), anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingDto));

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", owner.getId())
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "5")
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id", is(bookingDto.getId()), Integer.class))
                .andExpect(jsonPath("$[0].booker", is(bookingDto.getBooker()), User.class))
                .andExpect(jsonPath("$[0].item", is(bookingDto.getItem()), Item.class));

        verify(bookingService, times(1))
                .getAllByOwnerId(anyInt(), anyString(), anyInt(), anyInt());
    }

    @Test
    void createTest() throws Exception {
        when(bookingService.save(anyInt(), any(BookingDtoRequest.class)))
                .thenReturn(bookingDto);

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", "1")
                        .content(mapper.writeValueAsString(bookingDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Integer.class))
                .andExpect(jsonPath("$.booker", is(bookingDto.getBooker()), User.class))
                .andExpect(jsonPath("$.item", is(bookingDto.getItem()), Item.class));

        verify(bookingService, times(1))
                .save(anyInt(), any(BookingDtoRequest.class));
    }

    @Test
    void confirmationTest() throws Exception {
        when(bookingService.confirmation(anyInt(), anyInt(), anyBoolean()))
                .thenReturn(bookingDto);

        mockMvc.perform(patch("/bookings/{bookingId}", bookingDto.getId())
                        .header("X-Sharer-User-Id", "1")
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Integer.class))
                .andExpect(jsonPath("$.booker", is(bookingDto.getBooker()), User.class))
                .andExpect(jsonPath("$.item", is(bookingDto.getItem()), Item.class))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().toString())));

        verify(bookingService, times(1))
                .confirmation(anyInt(), anyInt(), anyBoolean());
    }
}