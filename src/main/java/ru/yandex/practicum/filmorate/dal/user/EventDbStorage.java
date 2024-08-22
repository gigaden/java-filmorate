package ru.yandex.practicum.filmorate.dal.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.BaseDbStorage;
import ru.yandex.practicum.filmorate.model.Event;

import java.util.List;

@Slf4j
@Repository
public class EventDbStorage extends BaseDbStorage<Event> implements EventStorage {

    private static final String SAVE_EVENT_QUERY = """
            INSERT INTO events(timestamp,user_id,event_type,operation,entity_id)
            VALUES (?,?,?,?,?)
            """;
    private static final String FIND_EVENTS_BY_USER_ID_QUERY = "SELECT * FROM events WHERE user_id = ?";

    public EventDbStorage(JdbcTemplate jdbc, RowMapper<Event> mapper) {
        super(jdbc, mapper);
    }

    /**
     * Создание записи события в базу данных.
     */
    @Override
    public void saveEvent(Event event) {
        log.info("Создание записи действия в базе данных: {}.", event);
        long id = insert(SAVE_EVENT_QUERY,
                event.getTimestamp(),
                event.getUserId(),
                event.getEventType().toString(),
                event.getOperation().toString(),
                event.getEntityId());
        event.setEventId(id);
        log.info("Создана запись действия в базе данных: {}.", event);
    }

    /**
     * Получение записей событий из базы данных.
     */
    @Override
    public List<Event> findEventsByUserId(Long userId) {
        log.info("Получение всех событий из базы данных Пользователя с ID: {}.", userId);
        List<Event> events = findMany(FIND_EVENTS_BY_USER_ID_QUERY, userId);
        log.info("Все события получены  из базы данных Пользователя с ID: {}.", userId);
        return events;
    }
}
