package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.Collection;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(InMemoryUserStorage inMemoryUserStorage, UserService userService) {
        this.userService = userService;
    }

    // Получаем всех пользователей
    @GetMapping
    public Collection<User> getAll() {
        return userService.getAll();
    }

    // Получаем пользователя
    @GetMapping("/{id}")
    public User get(@PathVariable Long id) {
        return userService.get(id);
    }

    // Обрабатываем запрос на добавление пользователя
    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return userService.create(user);
    }

    // Обрабатываем запрос на изменение пользователя
    @PutMapping
    public User update(@Valid @RequestBody User newUser) {
        return userService.update(newUser);
    }

    // Обрабатываем запрос на удаление пользователя
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }

    // Добавляем в друзья
    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userService.addFriend(id, friendId);
    }

    // Удаляем из друзей
    @DeleteMapping("/{id}/friends/{friendId}")
    public void delFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userService.delFriend(id, friendId);
    }

    // Возвращаем список друзей
    @GetMapping("/{id}/friends")
    public Collection<User> getFriends(@PathVariable Long id) {
        return userService.getFriends(id);
    }

    // Возвращаем список общих друзей
    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        return userService.getCommonFriends(id, otherId);
    }


}
