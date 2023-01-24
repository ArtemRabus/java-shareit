package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoOutput;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public ItemRequestDto create(int userId, ItemRequestDto itemRequestDto) {
        User requester = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id = %s not found", userId)));
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto, requester, LocalDateTime.now());
        return ItemRequestMapper.toItemRequestDto(itemRequestRepository.save(itemRequest));
    }

    @Override
    public List<ItemRequestDtoOutput> getAll(int userId) {
        var items = itemRequestRepository.findAllByRequesterId(userId);
        if (CollectionUtils.isEmpty(items)) {
            userRepository.findById(userId)
                    // Есть тест в постмане, который проверяет запрос по неверному id, для него важно отличать,
                    // если реквестов нет потому что не создавались этим юзером (200) или потому что пользователя не существует (404 или 500)
                    .orElseThrow(() -> new NotFoundException(String.format("User with id = %s not found", userId)));
        }
        return items
                .stream()
                .map(r -> ItemRequestMapper.toItemRequestDtoOut(r, getItems(r.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestDtoOutput> getAllOtherUser(int userId, int from, int size) {
        int page = from / size;
        return itemRequestRepository.findAllByRequesterIdNot(userId, PageRequest.of(page, size,
                        Sort.by(Sort.Direction.DESC, "created"))).stream()
                .map(r -> ItemRequestMapper.toItemRequestDtoOut(r, getItems(r.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequestDtoOutput getById(int userId, int requestId) {
        ItemRequest itemRequest = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(String.format("Request with id = %s not found", requestId)));
        if (userId != itemRequest.getRequester().getId()) {
            // здесь важно проверить, что если другой пользователь хочет посмотреть реквест, то он существует в базе.
            // избавляюсь от дубля запроса в том случае, если пользователь сам создатель реквеста (если его удалили - то запись реквеста тоже удалится.
            // совсем избавиться от дубля я не могу
            userRepository.findById(userId)
                    .orElseThrow(() -> new NotFoundException(String.format("User id %s not found", userId)));
        }
        return ItemRequestMapper.toItemRequestDtoOut(itemRequest, getItems(requestId));
    }

    private List<ItemDto> getItems(int id) {
        return itemRepository.findItemByItemRequestId(id).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
