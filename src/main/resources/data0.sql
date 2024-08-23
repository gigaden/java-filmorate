INSERT INTO mpas (name, description)
SELECT * FROM (VALUES ('G', 'У фильма нет возрастных ограничений'),
('PG', 'Детям рекомендуется смотреть фильм с родителями'),
('PG-13', 'Детям до 13 лет просмотр не желателен'),
('R', 'Лицам до 17 лет просматривать фильм можно только в присутствии взрослого'),
('NC-17', 'Лицам до 18 лет просмотр запрещён'))
WHERE NOT EXISTS (SELECT * FROM mpas);

INSERT INTO genres (name)
VALUES ('Комедия'), ('Драма'), ('Мультфильм'), ('Триллер'), ('Документальный'), ('Боевик')
ON CONFLICT DO NOTHING;
SELECT * FROM (VALUES ('Комедия'), ('Драма'), ('Мультфильм'), ('Триллер'), ('Документальный'), ('Боевик'))
WHERE NOT EXISTS (SELECT * FROM genres);


INSERT INTO films (name, description, releaseDate, duration, rating)
VALUES ('Avatar2', 'Very good movie', '2023-01-11', 240, 1),
('Avatar3', 'Very good movie', '2025-01-11', 220, 2)
ON CONFLICT DO NOTHING;