package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.film.DirectorStorage;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DirectorService {
    DirectorStorage directorStorage;

    @Autowired
    public DirectorService(DirectorStorage directorStorage) {
        this.directorStorage = directorStorage;
    }

    // Получаем список id всех режиссеров
    public Collection<Long> getAllDirectorsIds() {
        return directorStorage.getAll().stream().map(Director::getId).collect(Collectors.toList());
    }

    // Получаем список режиссеров по id фильма
    public Collection<Director> getAllDirectorsByFilmId(Long id) {
        return directorStorage.findAllByFilmId(id);
    }

}