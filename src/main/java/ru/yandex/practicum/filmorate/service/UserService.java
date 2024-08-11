package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.exception.ValidationNullException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private final UserStorage userStorage;
    private final LocalDate validDate = LocalDate.now();

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> getAll() {
        log.info("Получаем коллекцию всех пользователей.");
        return userStorage.getAll();
    }

    public User get(Long id) {
        log.info("Попытка получить пользователя с id={}", id);
        return userStorage.get(id)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id=%d не найден", id)));
    }

    public User create(User user) {
        log.info("Попытка создать пользователя");
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(validDate)) {
            log.warn("Указана дата рождения из будущего {}", user.getBirthday());
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
        user.setFriends(new HashSet<>());
        return userStorage.create(user);
    }

    public User update(User newUser) {
        log.info("Попытка обновить пользователя.");
        if (newUser.getId() == null) {
            log.warn("не указан Id пользователя.");
            throw new ValidationNullException("Id должен быть указан.");
        }
        if (newUser.getBirthday().isAfter(validDate)) {
            log.warn("Указана дата рождения из будущего {}", newUser.getBirthday());
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
        get(newUser.getId());
        return userStorage.update(newUser);
    }

    public void delete(Long id) {
        log.info("Попытка удалить пользователя с id={}.", id);
        get(id);
        userStorage.delete(id);
    }

    public void addFriend(Long id, Long friendId) {
        log.info("Добавляем пользователю id={} друга id={}", id, friendId);
        User user = get(id);
        User friend = get(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(id);
        log.info("Добавили пользователю id={} друга id={}", id, friendId);
    }

    public void delFriend(Long id, Long friendId) {
        log.info("Удаляем у пользователя id={} друга id={}", id, friendId);
        User user = get(id);
        User friend = get(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(id);
        log.info("Удалили у пользователя id={} друга id={}", id, friendId);
    }

    public Collection<User> getFriends(Long id) {
        log.info("Получаем всех друзей у пользователя id={}", id);
        return get(id).getFriends().stream().map(this::get).collect(Collectors.toList());
    }

    public Collection<User> getCommonFriends(Long id, Long otherId) {
        log.info("Ищем общих друзей у пользователя id={} и друга id={}", id, otherId);
        return get(id).getFriends().stream()
                .filter(i -> get(otherId).getFriends().contains(i))
                .map(this::get).collect(Collectors.toList());

    }
}
