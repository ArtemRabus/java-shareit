package ru.practicum.shareit.item.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ItemRepository {
    private List<Item> items = new ArrayList<>();
    private int id = 0;

    public List<Item> getAll(User owner) {
        return items.stream()
                .filter(i -> i.getOwner() == owner)
                .collect(Collectors.toList());
    }

    public Optional<Item> getById(int id) {
        return items.stream()
                .filter(i -> i.getId() == id)
                .findAny();
    }

    public Item save(Item item, User owner) {
        item.setId(getId());
        item.setOwner(owner);
        items.add(item);
        return item;
    }

    public Item edit(Item newItem, Item oldItem) {
        if (newItem.getName() != null) {
            oldItem.setName(newItem.getName());
        }
        if (newItem.getDescription() != null) {
            oldItem.setDescription(newItem.getDescription());
        }
        if (newItem.getAvailable() != null) {
            oldItem.setAvailable(newItem.getAvailable());
        }
        return oldItem;
    }

    public void delete(Item item) {
        items.remove(item);
    }

    public List<Item> search(String text) {
        String str = text.toLowerCase();
        Set<Item> itemListByName = items.stream()
                .filter(Item::getAvailable)
                .filter(i -> i.getName().toLowerCase().contains(str))
                .collect(Collectors.toSet());

        Set<Item> itemListByDescription = items.stream()
                .filter(Item::getAvailable)
                .filter(i -> i.getDescription().toLowerCase().contains(str))
                .collect(Collectors.toSet());
        itemListByName.addAll(itemListByDescription);
        return List.copyOf(itemListByName);
    }

    private int getId() {
        return ++id;
    }
}

