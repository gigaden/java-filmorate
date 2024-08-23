package ru.yandex.practicum.filmorate.dal.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {

    Collection<User> getAll();

    Optional<User> get(Long id);

    User create(User user);

    User update(User user);

    void delete(Long id);
}