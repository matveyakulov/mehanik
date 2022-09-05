CREATE SEQUENCE IF NOT EXISTS core.users_cars_id_seq START 1;

CREATE TABLE IF NOT EXISTS core.users_cars
(
    id      BIGINT DEFAULT nextval('core.users_cars_id_seq') NOT NULL,
    vin     varchar                                          null,
    type    varchar                                          null,
    brand   varchar                                          null,
    model   varchar                                          null,
    release varchar                                          null,
    photo   varchar                                          null,
    user_id BIGINT                                           NOT NULL,
    CONSTRAINT users_cars_pk PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES core.users (id)
);