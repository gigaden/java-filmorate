package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.user.UserStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.exception.ValidationNullException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.enums.EventType;
import ru.yandex.practicum.filmorate.model.enums.Operation;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private final UserStorage userStorage;
    private final FriendsService friendsService;
    private final LocalDate validDate = LocalDate.now();
    private final EventService eventService;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage,
                       FriendsService friendsService, EventService eventService) {
        this.userStorage = userStorage;
        this.friendsService = friendsService;
        this.eventService = eventService;
    }

    public Collection<User> getAll() {
        log.info("Получаем коллекцию всех пользователей.");
        Collection<User> users = userStorage.getAll();
        for (User user : users) {
            setUsersFriends(user);
        }
        log.info("Пользователи успешно переданы");
        return users;
    }

    public User get(Long id) {
        log.info("Попытка получить пользователя с id={}", id);
        User user = userStorage.get(id)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id=%d не найден", id)));
        setUsersFriends(user);
        log.info("Пользователь с id = {} успешно передан.", id);
        return user;
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
        User user = userStorage.update(newUser);
        log.info("Пользователь с id = {} успешно обновлён", user.getId());
        return user;
    }

    public void delete(Long id) {
        log.info("Попытка удалить пользователя с id={}.", id);
        get(id);
        userStorage.delete(id);
        log.info("Пользоавтель удалён id={}.", id);
    }

    public void addFriend(long id, long friendId) {
        log.info("Добавляем пользователю id={} друга id={}", id, friendId);
        User user = get(id);
        get(friendId);
        boolean friendship = false;
        if (id == friendId) {
            throw new NotFoundException("Нельзя добавить в друзья самого себя.");
        }

        if (user.getFriends() != null && user.getFriends().contains(friendId)) {
            friendship = true;
            friendsService.updateFriendship(id, friendId, friendship);
        }
        friendsService.addFriend(friendId, id, friendship);
        log.info("Добавили пользователю id={} друга id={}", id, friendId);
        eventService.createEvent(id, EventType.FRIEND, Operation.ADD, friendId);
    }

    public void delFriend(Long id, Long friendId) {
        log.info("Удаляем у пользователя id={} друга id={}", id, friendId);
        get(id);
        get(friendId);
        friendsService.deleteFriend(id, friendId);
        log.info("Удалили у пользователя id = {} друга id = {}", id, friendId);
        eventService.createEvent(id, EventType.FRIEND, Operation.REMOVE, friendId);
    }

    public Collection<User> getFriends(Long id) {
        log.info("Получаем всех друзей у пользователя id = {}", id);
        get(id);
        Collection<User> friends = friendsService.getAllFriends(id).stream()
                .peek(this::setUsersFriends).collect(Collectors.toList());
        log.info("Коллекция всех друзей пользователя id = {} передана", id);
        return friends;
    }

    public Collection<User> getCommonFriends(Long id, Long otherId) {
        log.info("Ищем общих друзей у пользователя id={} и друга id={}", id, otherId);
        Collection<User> commonFriends = get(id).getFriends().stream()
                .filter(i -> get(otherId).getFriends().contains(i))
                .map(this::get).collect(Collectors.toList());
        log.info("Коллекция общих друзей id = {} с id = {} передана", id, otherId);
        return commonFriends;

    }

    private void setUsersFriends(User user) {
        user.setFriends(new HashSet<>(friendsService.getUsersFriendsById(user.getId())));
    }

}
