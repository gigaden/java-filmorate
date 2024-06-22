package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.exception.ValidationNullException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final Map<Long, User> users = new HashMap<>();
    private final LocalDate validDate = LocalDate.now();

    // Получаем всех пользователей
    @GetMapping
    public Collection<User> getAll() {
        log.info("Коллекция пользователей успешно получена");
        return users.values();
    }

    // Обрабатываем запрос на добавление пользователя
    @PostMapping
    public User create(@Valid @RequestBody User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(validDate)) {
            log.warn("Указана дата рождения из будущего {}", user.getBirthday());
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("Пользователь с id={} успешно добавлен", user.getId());
        return user;

    }

    // Обрабатываем запрос на изменение пользователя
    @PutMapping
    public User update(@Valid @RequestBody User newUser) {

        if (newUser.getId() == null) {
            log.warn("не указан Id пользователя");
            throw new ValidationNullException("Id должен быть указан");
        }
        if (newUser.getBirthday().isAfter(validDate)) {
            log.warn("Указана дата рождения из будущего {}", newUser.getBirthday());
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());
            oldUser.setName(newUser.getName() == null || newUser.getName().isBlank() ?
                    newUser.getLogin() : newUser.getName());
            oldUser.setLogin(newUser.getLogin());
            oldUser.setEmail(newUser.getEmail());
            oldUser.setBirthday(newUser.getBirthday());
            log.info("Информация о пользователе с id={} успешно обновлена", oldUser.getId());
            return oldUser;
        }
        log.warn("Пользователь с id={} не найден", newUser.getId());
        throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
    }


    // Метод для генерации идентификатора нового поста
    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
