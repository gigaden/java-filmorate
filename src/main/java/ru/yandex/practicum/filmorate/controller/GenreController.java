package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.Collection;

@RestController
@RequestMapping("/genres")
public class GenreController {

    private final GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    // Обрабатываем запрос на получение всех фильмов
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<GenreDto> getAll() {
        return genreService.getAll();
    }

    // Получаем фильм по id
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public GenreDto get(@PathVariable int id) {
        return genreService.getGenreById(id);
    }



}