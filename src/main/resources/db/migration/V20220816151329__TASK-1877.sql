CREATE SCHEMA IF NOT EXISTS core;

CREATE SEQUENCE IF NOT EXISTS core.users_id_seq START 1;

CREATE TABLE IF NOT EXISTS core.users
(
    id         BIGINT          DEFAULT nextval('core.users_id_seq') NOT NULL,
    name       varchar                                              NOT NULL,
    site       varchar                                              NOT NULL,
    phone      varchar                                              NOT NULL,
    city       varchar                                              NOT NULL,
    email      varchar                                              NOT NULL,
    rating     numeric(29, 10) DEFAULT 0,
    is_company bool                                                 NOT NULL,
    photo      varchar                                              NULL,
    CONSTRAINT users_pk PRIMARY KEY (id)
);

