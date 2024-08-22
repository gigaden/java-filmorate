package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.dto.DirectorDto;
import ru.yandex.practicum.filmorate.model.Director;

public class DirectorMapper {
    public static DirectorDto mapToDirectorDto(Director director) {

        return DirectorDto.builder().id(director.getId())
                .name(director.getName())
                .build();
    }
}