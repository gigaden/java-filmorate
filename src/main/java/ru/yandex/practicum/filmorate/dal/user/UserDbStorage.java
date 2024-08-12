package ru.yandex.practicum.filmorate.dal.user;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.film.BaseDbStorage;
import ru.yandex.practicum.filmorate.dal.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

@Repository("userDbStorage")
public class UserDbStorage extends BaseDbStorage<User> implements UserStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM users";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM users WHERE id = ?";
    private static final String INSERT_QUERY = "INSERT INTO users(email, login, name," +
            "birthday)" +
            "VALUES (?,?,?,?)";
    private static final String UPDATE_QUERY = "UPDATE users SET email = ?, login = ?," +
            " name = ?, birthday = ? WHERE id = ?";
    private static final String DELETE_BY_ID = "DELETE from users WHERE id = ?";

    public UserDbStorage(JdbcTemplate jdbc, UserRowMapper mapper) {
        super(jdbc, mapper);
    }


    // Получаем всех пользователей
    @Override
    public Collection<User> getAll() {
        return findMany(FIND_ALL_QUERY);
    }

    // Получаем пользователя по id
    @Override
    public Optional<User> get(Long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    // Создаём нового пользователя
    @Override
    public User create(User user) {
        long id = insert(INSERT_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());
        user.setId(id);
        return user;
    }

    // Обновляем пользователя
    @Override
    public User update(User user) {
        update(UPDATE_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId()
        );
        return user;
    }

    // Удаляем пользователя
    @Override
    public void delete(Long id) {
        delete(DELETE_BY_ID, id);
    }
}
