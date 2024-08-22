INSERT INTO mpas (name)
VALUES ('G'), ('PG'), ('PG-13'), ('R'), ('NC-17')
ON CONFLICT DO NOTHING;

INSERT INTO genres (name)
VALUES ('Комедия'), ('Драма'), ('Мультфильм'), ('Триллер'), ('Документальный'), ('Боевик')
ON CONFLICT DO NOTHING;

INSERT INTO films (name, description, releaseDate, duration, rating)
VALUES ('Avatar2', 'Very good movie', '2023-01-11', 240, 1),
('Avatar3', 'Very good movie', '2025-01-11', 220, 2)
ON CONFLICT DO NOTHING;