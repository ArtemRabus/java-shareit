package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {
    private List<User> users = new ArrayList<>();
    private int id = 0;

    public List<User> getAll() {
        return users;
    }

    public Optional<User> getById(int id) {
        return users.stream()
                .filter(u -> u.getId() == id)
                .findAny();
    }

    public User save(User user) {
        user.setId(getId());
        users.add(user);
        return user;
    }

    public User edit(User newUser, User oldUser) {
        if (newUser.getName() != null) {
            oldUser.setName(newUser.getName());
        }
        if (newUser.getEmail() != null) {
            oldUser.setEmail(newUser.getEmail());
        }
        return oldUser;
    }

    public void delete(User user) {
        users.remove(user);
    }

    private int getId() {
        return ++id;
    }

    public boolean contains(String email) {
        for (User user : users) {
            if (user.getEmail().equals(email))
                return true;
        }
        return false;
    }
}

