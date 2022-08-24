CREATE SCHEMA IF NOT EXISTS core;

CREATE SEQUENCE IF NOT EXISTS core.users_id_seq START 1;

CREATE TABLE IF NOT EXISTS core.users
(
    id           BIGINT          DEFAULT nextval('core.users_id_seq') NOT NULL,
    name         varchar                                              NULL,
    site         varchar                                              NULL,
    phone        varchar                                              NOT NULL,
    city         varchar                                              NULL,
    email        varchar                                              NULL,
    rating       numeric(29, 10) DEFAULT 0,
    count_rating BIGINT          DEFAULT 0,
    is_company   bool                                                 NULL,
    photo        varchar                                              NULL,
    CONSTRAINT users_pk PRIMARY KEY (id)
);

