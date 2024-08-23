package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.EventService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final EventService eventService;

    @Autowired
    public UserController(UserService userService,EventService eventService) {
        this.userService = userService;
        this.eventService = eventService;
    }

    /**
     * Получение записей событий из базы данных.
     */
    @GetMapping("/{userId}/feed")
    @ResponseStatus(HttpStatus.OK)
    public List<Event> getEvent(@PathVariable Long userId) {
        return eventService.findEventsByUserId(userId);
    }

    // Получаем всех пользователей
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<User> getAll() {
        return userService.getAll();
    }

    // Получаем пользователя по id
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User get(@PathVariable Long id) {
        return userService.get(id);
    }

    // Обрабатываем запрос на добавление пользователя
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@Valid @RequestBody User user) {
        return userService.create(user);
    }

    // Обрабатываем запрос на изменение пользователя
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public User update(@Valid @RequestBody User newUser) {
        return userService.update(newUser);
    }

    // Обрабатываем запрос на удаление пользователя
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Long userId) {
        userService.delete(userId);
    }

    // Добавляем в друзья
    @PutMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public void addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userService.addFriend(id, friendId);
    }

    // Удаляем из друзей
    @DeleteMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userService.delFriend(id, friendId);
    }

    // Возвращаем список друзей
    @GetMapping("/{id}/friends")
    @ResponseStatus(HttpStatus.OK)
    public Collection<User> getFriends(@PathVariable Long id) {
        return userService.getFriends(id);
    }

    // Возвращаем список общих друзей
    @GetMapping("/{id}/friends/common/{otherId}")
    @ResponseStatus(HttpStatus.OK)
    public Collection<User> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        return userService.getCommonFriends(id, otherId);
    }


}
