ALTER TABLE IF EXISTS core.service_announcements
    ADD COLUMN owner_id BIGINT REFERENCES core.users (id);