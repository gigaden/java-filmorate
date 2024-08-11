package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.GenreStorage;
import ru.yandex.practicum.filmorate.storage.film.LikeStorage;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GenreService {
    GenreStorage genreStorage;

    @Autowired
    public GenreService(GenreStorage genreStorage, LikeStorage likeStorage) {
        this.genreStorage = genreStorage;
    }

    public Collection<Integer> getAllGenresID() {
        return genreStorage.getAll().stream().map(Genre::getId).collect(Collectors.toList());
    }

    public Collection<Genre> getAllFilmGenreById(Long id) {
        return genreStorage.findAllByFilmId(id);
    }

    public void addGenreToFilm(long id, Set<Genre> genres) {
        for (Genre genre : genres) {
            genreStorage.insertIntoFilmGenre(id, genre.getId());
        }
    }


}
