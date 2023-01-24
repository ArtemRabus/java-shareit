package ru.practicum.shareit.request.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class ItemRequestRepositoryTest {
    @Autowired
    ItemRequestRepository itemRequestRepository;
    @Autowired
    UserRepository userRepository;
    User requester;
    ItemRequest itemRequest;

    @BeforeEach
    void init() {
        requester = userRepository.save(new User(1, "req", "re@mail"));
        itemRequest = itemRequestRepository.save(new ItemRequest(1, "desc", requester, LocalDateTime.now()));
    }

    @Test
    void findAllByRequesterIdTest() {
        List<ItemRequest> res = itemRequestRepository.findAllByRequesterId(requester.getId());

        assertNotNull(res);
        assertEquals(1, res.size());
        assertEquals(itemRequest.getDescription(), res.get(0).getDescription());
    }
}