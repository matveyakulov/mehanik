CREATE SEQUENCE IF NOT EXISTS core.parts_announcement_id_seq START 1;

CREATE TABLE IF NOT EXISTS core.parts_announcement
(
    id             BIGINT DEFAULT nextval('core.parts_announcement_id_seq') NOT NULL,
    type           varchar                                                  NOT NULL,
    brand          varchar                                                  NOT NULL,
    generation     varchar                                                  NOT NULL,
    model          varchar                                                  NOT NULL,
    name_of_part   varchar                                                  NOT NULL,
    number_of_part varchar                                                  NOT NULL,
    condition      bool                                                     NOT NULL,
    original       bool                                                     NOT NULL,
    description    text                                                     NULL,
    price          int                                                      NULL,
    address        varchar                                                  NULL,
    photo          varchar                                                  NULL,
    date_create    timestamp without time zone                              NULL,
    use_email      bool   default true                                      NULL,
    use_phone      bool   default true                                      NULL,
    use_whatsapp   bool   default true                                      NULL,
    archive        bool   default false                                     NULL,
    owner_id       BIGINT                                                   NOT NULL,
    CONSTRAINT parts_announcement_pk PRIMARY KEY (id),
    FOREIGN KEY (owner_id) REFERENCES core.users (id)
);