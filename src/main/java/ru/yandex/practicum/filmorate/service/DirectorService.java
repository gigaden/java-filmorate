package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.film.DirectorStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.exception.ValidationNullException;
import ru.yandex.practicum.filmorate.model.Director;


import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DirectorService {
    DirectorStorage directorStorage;

    @Autowired
    public DirectorService(DirectorStorage directorStorage) {
        this.directorStorage = directorStorage;
    }

    // Получаем режиссера по id
    public Director getDirectorById(long id) {
        log.info("Попытка получить режиссера с id={}.", id);
        Director director = directorStorage.getDirectorById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Режиссер с id=%d не найден.", id)));

        log.info("Режиссер с id = {} успешно передан.", id);
        return director;
    }

    // Получаем список всех режиссеров
    public Collection<Director> getAll() {
        log.info("Попытка получить коллекцию режиссеров.");
        Collection<Director> directors = directorStorage.getAll();
        log.info("Коллекция режиссеров успешно передана.");
        return directors;
    }

    // Обновляем режиссера
    public Director update(Director newDirector) {
        log.info("Попытка обновить режиссера");
        if (newDirector.getId() == null) {
            log.warn("Id режиссера не указан.");
            throw new ValidationNullException("Id режиссера должен быть указан.");
        }
        getDirectorById(newDirector.getId());
        return directorStorage.update(newDirector);
    }

    // Добавляем режиссера
    public Director create(Director director) {
        log.info("Попытка создать режиссера");
        if (director.getName().isBlank()) {
            log.warn("Имя режиссера не может быть пустым.");
            throw new ValidationException("Имя режиссера не может быть пустым.");
        }
        return directorStorage.create(director);
    }

    // Получаем список id всех режиссеров
    public Collection<Long> getAllDirectorsIds() {
        return directorStorage.getAll().stream().map(Director::getId).collect(Collectors.toList());
    }

    // Получаем список режиссеров по id фильма
    public Collection<Director> getAllDirectorsByFilmId(Long filmId) {
        return directorStorage.findAllByFilmId(filmId);
    }

    // Добавляем режиссера к фильму в сводную таблицу
    public void addDirectorToFilm(long id, Set<Director> directors) {
        for (Director director : directors) {
            directorStorage.insertIntoFilmDirector(id, director.getId());
        }
    }

    // Удаляем режиссера по id
    public void delete(Long id) {
        log.info("Попытка удалить режиссера с id={}.", id);
        getDirectorById(id);
        directorStorage.delete(id);
        log.info("Режиссер с id = {} удалён.", id);
    }
}