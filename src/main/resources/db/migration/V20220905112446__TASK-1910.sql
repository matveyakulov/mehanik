ALTER TABLE IF EXISTS core.users
    ADD COLUMN count_rating BIGINT DEFAULT 0;

create or replace function core.refresh_rating() returns trigger as
$refresh_rating_tg$
begin
    UPDATE core.users
    SET count_rating = count_rating + 1,
        rating = ((rating * count_rating + new.value) / (count_rating + 1))
    WHERE id = new.user_to_id;
    RETURN NULL;
end;
$refresh_rating_tg$ LANGUAGE plpgsql;
CREATE TRIGGER refresh_rating_tg
    AFTER INSERT
    ON core.users_rating
    FOR EACH ROW
EXECUTE PROCEDURE core.refresh_rating();