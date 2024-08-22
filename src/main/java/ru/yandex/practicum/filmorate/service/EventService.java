package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.user.EventStorage;
import ru.yandex.practicum.filmorate.dal.user.UserDbStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.enums.EventType;
import ru.yandex.practicum.filmorate.model.enums.Operation;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
public class EventService {

    private final EventStorage eventDbStorage;
    private final UserDbStorage userDbStorage;

    @Autowired
    public EventService(EventStorage eventDbStorage, UserDbStorage userDbStorage) {
        this.eventDbStorage = eventDbStorage;
        this.userDbStorage = userDbStorage;

    }

    public void addEvent(Event event) {
        event.setTimestamp(Instant.now().toEpochMilli());
        eventDbStorage.saveEvent(event);
    }

    public List<Event> findEventsByUserId(Long userId) {
        userDbStorage.get(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id=%d не найден", userId)));
        return eventDbStorage.findEventsByUserId(userId);
    }

    public void createEvent(Long userId, EventType eventType, Operation operation, Long entityId) {
        Event event = Event.builder()
                .userId(userId)
                .eventType(eventType)
                .operation(operation)
                .entityId(entityId)
                .build();
        addEvent(event);
    }
}
