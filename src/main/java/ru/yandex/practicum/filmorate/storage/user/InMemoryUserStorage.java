package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component("inMemoryUserStorage")
@Slf4j
@Deprecated
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();

    @Override
    public Collection<User> getAll() {
        log.info("Коллекция пользователей успешно получена.");
        return users.values();
    }

    @Override
    public Optional<User> get(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public User create(User user) {
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("Пользователь с id={} успешно создан.", user.getId());
        return user;
    }

    @Override
    public User update(User newUser) {
        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());
            oldUser.setName(newUser.getName() == null || newUser.getName().isBlank() ?
                    newUser.getLogin() : newUser.getName());
            oldUser.setLogin(newUser.getLogin());
            oldUser.setEmail(newUser.getEmail());
            oldUser.setBirthday(newUser.getBirthday());
            log.info("Информация о пользователе с id={} успешно обновлена.", oldUser.getId());
            return oldUser;
        }
        log.warn("Пользователь с id={} не найден.", newUser.getId());
        throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден.");
    }

    @Override
    public void delete(Long id) {
        users.remove(id);
        log.info("Пользователь с id={} удалён.", id);
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
