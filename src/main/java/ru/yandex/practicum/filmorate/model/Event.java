package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.model.enums.EventType;
import ru.yandex.practicum.filmorate.model.enums.Operation;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    private Long timestamp;
    private Long userId;
    private EventType eventType;
    private Operation operation;
    private Long eventId;
    private Long entityId;
}