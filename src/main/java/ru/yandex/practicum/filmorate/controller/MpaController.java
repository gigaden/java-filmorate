package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.Collection;

@RestController
@RequestMapping("/mpa")
public class MpaController {

    private final MpaService mpaService;

    @Autowired
    public MpaController(MpaService mpaService) {
        this.mpaService = mpaService;
    }

    // Обрабатываем запрос на получение всех рейтингов
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<MpaDto> getAll() {
        return mpaService.getAllDtoMpa();
    }

    // Получаем рейтинг по id
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MpaDto get(@PathVariable int id) {
        return mpaService.getDto(id);
    }


}
