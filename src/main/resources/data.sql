INSERT INTO mpas (name, description)
SELECT * FROM (VALUES ('G', 'У фильма нет возрастных ограничений'),
('PG', 'Детям рекомендуется смотреть фильм с родителями'),
('PG-13', 'Детям до 13 лет просмотр не желателен'),
('R', 'Лицам до 17 лет просматривать фильм можно только в присутствии взрослого'),
('NC-17', 'Лицам до 18 лет просмотр запрещён'))
WHERE NOT EXISTS (SELECT * FROM mpas);

INSERT INTO genres (name)
SELECT * FROM (VALUES ('Комедия'), ('Драма'), ('Мультфильм'), ('Триллер'), ('Документальный'), ('Боевик'))
WHERE NOT EXISTS (SELECT * FROM genres);

INSERT INTO films (name, description, releaseDate, duration, mpa)
SELECT * FROM (VALUES ('Avatar2', 'Very good movie', '2023-01-11', 240, 1),
('Avatar3', 'Very good movie', '2025-01-11', 220, 2))
WHERE NOT EXISTS (SELECT * FROM films);


INSERT INTO film_genre
SELECT * FROM (VALUES (1, 1), (1, 3), (1, 4), (2, 5), (2, 6))
WHERE NOT EXISTS (SELECT * FROM film_genre);


INSERT INTO users (email, login, name, birthday)
SELECT * FROM (VALUES ('gig@ya.ru', 'gigaden', 'den', '1987-05-29'),
('alle111@ya.ru', 'alle111', 'elmira', '1986-11-01'),
('bob@ya.ru', 'bobich', 'bob', '2025-05-25')
)
WHERE NOT EXISTS (SELECT * FROM users);

INSERT INTO likes
SELECT * FROM ( VALUES (1, 3), (1, 2), (1,1), (2,2))
WHERE NOT EXISTS (SELECT * FROM likes);

INSERT INTO directors(name)
SELECT * FROM ( VALUES ('Spilberg'), ('Кустурица'), ('Джордж Лукас'))
WHERE NOT EXISTS (SELECT * FROM directors);

INSERT INTO film_director
SELECT * FROM (VALUES (1, 1), (1, 3), (1, 2), (2, 3), (2, 1))
WHERE NOT EXISTS (SELECT * FROM film_director);