CREATE TABLE IF NOT EXISTS mpas (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(40),
    description VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS films (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(40) NOT NULL,
    description VARCHAR(255) NOT NULL,
    releaseDate DATE NOT NULL,
    duration INTEGER NOT NULL,
    mpa INTEGER,
    constraint fk_film_mpa foreign key (mpa) REFERENCES PUBLIC.mpas (id) on delete cascade
);

CREATE TABLE IF NOT EXISTS genres (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(40) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS film_genre (
    film_id BIGINT,
    genre_id BIGINT,
    constraint fk_film_genre_film foreign key (film_id) references PUBLIC.films(id) on delete cascade,
    constraint fk_film_genre_genre foreign key (genre_id) references PUBLIC.genres(id) on delete cascade
);

CREATE TABLE IF NOT EXISTS users (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email VARCHAR(100) NOT NULL,
    login VARCHAR(40) NOT NULL,
    name VARCHAR(40),
    birthday DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS friends (
    user_id BIGINT,
    friend_id BIGINT,
    friendship BOOLEAN default false,
    constraint fk_user_users foreign key (user_id) references PUBLIC.users (id) on delete cascade,
    constraint fk_friend_users foreign key (friend_id) references PUBLIC.users (id) on delete cascade
);

CREATE TABLE IF NOT EXISTS likes (
    films_id BIGINT,
    users_id BIGINT,
    constraint fk_likes_films foreign key (films_id) references PUBLIC.films (id) on delete cascade,
    constraint fk_likes_users foreign key (users_id) references PUBLIC.USERS (id) on delete cascade
);