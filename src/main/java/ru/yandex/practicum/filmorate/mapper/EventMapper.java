package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.model.Event;

public class EventMapper {
    public static Event mapToEvent(Event event) {
        return Event.builder()
                .timestamp(event.getTimestamp())
                .userId(event.getUserId())
                .eventType(event.getEventType())
                .operation(event.getOperation())
                .eventId(event.getEventId())
                .entityId(event.getEntityId())
                .build();
    }
}