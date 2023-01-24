package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class ItemDto {
    private int id;
    private String name;
    private String description;
    private Boolean available;
    private Integer requestId;
}