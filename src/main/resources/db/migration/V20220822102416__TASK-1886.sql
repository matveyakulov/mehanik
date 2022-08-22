CREATE SEQUENCE IF NOT EXISTS core.users_rating_id_seq START 1;

CREATE TABLE IF NOT EXISTS core.users_rating
(
    id           BIGINT DEFAULT nextval('core.users_rating_id_seq') NOT NULL,
    value        int                                                NOT NULL,
    user_from_id BIGINT                                             NULL,
    user_to_id   BIGINT                                             NULL,
    CONSTRAINT users_rating_pk PRIMARY KEY (id),
    FOREIGN KEY (user_from_id) REFERENCES core.users (id),
    FOREIGN KEY (user_to_id) REFERENCES core.users (id)
);