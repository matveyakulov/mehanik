CREATE SEQUENCE IF NOT EXISTS core.parts_announcement_favorites_id_seq START 1;

CREATE TABLE IF NOT EXISTS core.parts_announcement_favorites
(
    id                    BIGINT DEFAULT nextval('core.parts_announcement_favorites_id_seq') NOT NULL,
    user_id               BIGINT                                                             NOT NULL,
    parts_announcement_id BIGINT                                                             NOT NULL,
    CONSTRAINT parts_announcement_favorites_pk PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES core.users (id),
    FOREIGN KEY (parts_announcement_id) REFERENCES core.parts_announcement (id)
);