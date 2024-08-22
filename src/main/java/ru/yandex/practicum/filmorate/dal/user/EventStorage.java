package ru.yandex.practicum.filmorate.dal.user;

import ru.yandex.practicum.filmorate.model.Event;

import java.util.List;

public interface EventStorage {

    void saveEvent(Event event);

    List<Event> findEventsByUserId(Long userId);
}
