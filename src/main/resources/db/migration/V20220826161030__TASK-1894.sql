ALTER TABLE IF EXISTS core.parts_announcement
    ADD COLUMN city varchar NOT NULL;
ALTER TABLE IF EXISTS core.parts_announcement
    ADD COLUMN latitude numeric(29, 10) NOT NULL;
ALTER TABLE IF EXISTS core.parts_announcement
    ADD COLUMN longitude numeric(29, 10) NOT NULL;
ALTER TABLE IF EXISTS core.parts_announcement
    ADD COLUMN is_company boolean NOT NULL;