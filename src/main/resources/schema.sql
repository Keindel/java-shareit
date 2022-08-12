CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    user_name  VARCHAR(50)                                     NOT NULL,
    email VARCHAR(50)                                     NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS requests
(
    id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    description  varchar(500) NOT NULL,
    requester_id BIGINT REFERENCES users (id) ON DELETE CASCADE,
    created      timestamp    NOT NULL
);

CREATE TABLE IF NOT EXISTS items
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    item_name        VARCHAR(50)  NOT NULL,
    description VARCHAR(500) NOT NULL,
    owner_id    BIGINT       NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    available   BOOLEAN      NOT NULL,
    request_id  BIGINT REFERENCES requests (id)
);

CREATE TABLE IF NOT EXISTS bookings
(
    id        BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    start_date     TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    end_date       TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    item_id   BIGINT REFERENCES items (id) ON DELETE CASCADE,
    booker_id BIGINT REFERENCES users (id) ON DELETE CASCADE,
    status    varchar(50)
);

CREATE TABLE IF NOT EXISTS comments
(
    id        BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    comment_text      varchar(200) NOT NULL,
    item_id   BIGINT REFERENCES items (id) ON DELETE CASCADE,
    author_id BIGINT REFERENCES users (id) ON DELETE CASCADE
);