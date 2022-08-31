CREATE SEQUENCE IF NOT EXISTS core.service_announcements_id_seq START 1;

CREATE TABLE IF NOT EXISTS core.service_announcements
(
    id               BIGINT          DEFAULT nextval('core.service_announcements_id_seq') NOT NULL,
    service          varchar                                                              not null,
    company_name     varchar                                                              not null,
    description      varchar                                                              null,
    address          varchar                                                              not null,
    latitude         numeric(29, 10)                                                      not null,
    longitude        numeric(29, 10)                                                      not null,
    rating           numeric(29, 10) default 0,
    count_rating_row BIGINT          default 0,
    CONSTRAINT service_announcements_pk PRIMARY KEY (id)
);

CREATE SEQUENCE IF NOT EXISTS core.service_announcements_photo_id_seq START 1;

CREATE TABLE IF NOT EXISTS core.service_announcements_photo
(
    id                      BIGINT DEFAULT nextval('core.service_announcements_photo_id_seq') NOT NULL,
    service_announcement_id BIGINT                                                            not null,
    photo                   varchar                                                           not null,
    CONSTRAINT service_announcements_photo_pk PRIMARY KEY (id),
    FOREIGN KEY (service_announcement_id) REFERENCES core.service_announcements (id)
);

CREATE SEQUENCE IF NOT EXISTS core.service_announcements_car_type_id_seq START 1;

CREATE TABLE IF NOT EXISTS core.service_announcements_car_type
(
    id                      BIGINT DEFAULT nextval('core.service_announcements_car_type_id_seq') NOT NULL,
    service_announcement_id BIGINT                                                               not null,
    type                    varchar                                                              not null,
    CONSTRAINT service_announcements_car_type_pk PRIMARY KEY (id),
    FOREIGN KEY (service_announcement_id) REFERENCES core.service_announcements (id)
);

CREATE SEQUENCE IF NOT EXISTS core.service_announcements_car_brand_id_seq START 1;

CREATE TABLE IF NOT EXISTS core.service_announcements_car_brand
(
    id                      BIGINT DEFAULT nextval('core.service_announcements_car_brand_id_seq') NOT NULL,
    service_announcement_id BIGINT                                                                not null,
    brand                   varchar                                                               not null,
    CONSTRAINT service_announcements_car_brand_pk PRIMARY KEY (id),
    FOREIGN KEY (service_announcement_id) REFERENCES core.service_announcements (id)
);