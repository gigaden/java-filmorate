package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.DirectorDto;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import java.util.Collection;


@RestController
@RequestMapping("/directors")
public class DirectorController {

    private final DirectorService directorService;

    @Autowired
    public DirectorController(DirectorService directorService) {
        this.directorService = directorService;
    }

    // Обрабатываем запрос на получение всех режиссеров
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<DirectorDto> getAll() {
        return directorService.getAll();
    }

    // Получаем режиссера по id
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Director get(@PathVariable long id) {
        return directorService.getDirectorById(id);
    }

    // Обрабатываем запрос на добавление режиссера
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DirectorDto create(@Valid @RequestBody Director director) {
        return directorService.create(director);
    }

    // Обрабатываем запрос на изменение режиссера
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public DirectorDto update(@Valid @RequestBody Director newDirector) {
        return directorService.update(newDirector);
    }

    // Обрабатываем запрос на удаление режиссера по id
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Long id) {
        directorService.delete(id);
    }
}