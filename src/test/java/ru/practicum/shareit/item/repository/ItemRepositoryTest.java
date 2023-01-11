package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class ItemRepositoryTest {
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ItemRequestRepository itemRequestRepository;
    User user;
    Item item;
    ItemRequest itemRequest;

    @BeforeEach
    void init() {
        user = userRepository.save(new User(1, "user", "u@mail"));
        itemRequest = itemRequestRepository.save(new ItemRequest(1, "req", user, LocalDateTime.now()));
        item = itemRepository.save(new Item(1, "item", "descr", true, user.getId(), itemRequest));
    }

    @Test
    void searchTest() {
        Page<Item> res = itemRepository.search("item", Pageable.unpaged());
        assertNotNull(res);
        assertEquals(item, res.stream().findFirst().orElseThrow());
    }

    @Test
    void findAllByOwnerIdTest() {
        Page<Item> res = itemRepository.findAllByOwnerId(user.getId(), Pageable.unpaged());
        assertNotNull(res);
        assertEquals(1, res.getTotalElements());
    }

    @Test
    void findItemByItemRequestIdTest() {
        List<Item> res = itemRepository.findItemByItemRequestId(itemRequest.getId());
        assertNotNull(res);
        assertEquals(1, res.size());
    }
}