package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final UserService userService;

    public List<Item> getAll(int userId) {
        User owner = userService.getById(userId);
        return itemRepository.getAll(owner);
    }

    public ItemDto getById(int id) {
        Item item = itemRepository.getById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Item with id = %s not found", id)));
        return itemMapper.toItemDto(item);
    }

    public ItemDto save(ItemDto itemDto, int userId) {
        User owner = userService.getById(userId);
        Item item = itemRepository.save(itemMapper.toItem(itemDto), owner);
        return itemMapper.toItemDto(item);
    }

    public ItemDto edit(ItemDto itemDto, int userId, int itemId) {
        User owner = userService.getById(userId);
        Item oldItem = itemRepository.getById(itemId).get();
        if (oldItem.getOwner() == owner) {
            Item newItem = itemRepository.edit(itemMapper.toItem(itemDto), oldItem);
            return itemMapper.toItemDto(newItem);
        }
        throw new NotFoundException("Only the owner of the item can edit information about it");
    }

    public void delete(int id) {
        ItemDto item = getById(id);
        itemRepository.delete(itemMapper.toItem(item));
    }

    public List<Item> search(String text, int userId) {
        userService.getById(userId);
        List<Item> listItem = new ArrayList<>();
        if (text.length() != 0) {
            listItem = itemRepository.search(text);
        }
        return listItem;
    }

}
