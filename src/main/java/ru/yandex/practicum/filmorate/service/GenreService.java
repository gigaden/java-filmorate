package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.film.GenreStorage;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GenreService {
    GenreStorage genreStorage;

    @Autowired
    public GenreService(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public GenreDto getGenreById(int id) {
        log.info("Попытка получить жанр с id={}.", id);
        GenreDto genre = genreStorage.getGenreById(id)
                .map(GenreMapper::mapToGenreDto)
                .orElseThrow(() -> new NotFoundException(String.format("Фильм с id=%d не найден.", id)));
        log.info("Жанр с id = {} успешно передан.", id);
        return genre;
    }

    // Получаем список всех жанров
    public Collection<GenreDto> getAll() {
        log.info("Попытка получить коллекцию жанров.");
        Collection<GenreDto> genreDtos = genreStorage.getAll()
                .stream().map(GenreMapper::mapToGenreDto).collect(Collectors.toList());
        log.info("Коллекция жанров успешно передана.");
        return genreDtos;
    }

    // Получаем список id всех жанров.
    public Collection<Integer> getAllGenresID() {
        return genreStorage.getAll().stream().map(Genre::getId).collect(Collectors.toList());
    }

    // Получаем список жанров фильма по id
    public Collection<Genre> getAllFilmGenreById(Long id) {
        return genreStorage.findAllByFilmId(id);
    }

    // Добавляем жанр к фильму в сводную таблицу
    public void addGenreToFilm(long id, Set<Genre> genres) {
        for (Genre genre : genres) {
            genreStorage.insertIntoFilmGenre(id, genre.getId());
        }
    }


}
