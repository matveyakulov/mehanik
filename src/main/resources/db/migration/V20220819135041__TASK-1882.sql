CREATE SCHEMA IF NOT EXISTS users;

CREATE SEQUENCE IF NOT EXISTS users.roles_id_seq start 1;

CREATE TABLE IF NOT EXISTS users.roles
(
    id   bigint                                              NOT NULL DEFAULT nextval('users.roles_id_seq'::regclass),
    name character varying(450) COLLATE pg_catalog."default" NOT NULL
);

COMMENT ON TABLE users.roles IS 'Роли';
COMMENT ON COLUMN users.roles.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN users.roles.name IS 'Название роли';

CREATE SEQUENCE IF NOT EXISTS users.sessions_id_seq start 1;

CREATE TABLE IF NOT EXISTS users.sessions
(
    id            bigint                      DEFAULT nextval('users.sessions_id_seq'::regclass) NOT NULL,
    del           boolean                     DEFAULT false,
    date_create   timestamp without time zone DEFAULT now(),
    date_update   timestamp without time zone,
    last_user_id  bigint,
    user_id       bigint                                                                         NOT NULL,
    access_token  character varying                                                              NOT NULL,
    refresh_token character varying                                                              NOT NULL,
    last_login    timestamp without time zone,
    useragent     character varying,
    userIp        character varying
);

COMMENT ON TABLE users.sessions IS 'Сессии пользователей';
COMMENT ON COLUMN users.sessions.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN users.sessions.del IS 'Метка для удалённых записей';
COMMENT ON COLUMN users.sessions.date_create IS 'Дата и время создания записи';
COMMENT ON COLUMN users.sessions.date_update IS 'Дата и время последнего изменения записи';
COMMENT ON COLUMN users.sessions.last_user_id IS 'Идентификатор пользователя, изменившего запись';
COMMENT ON COLUMN users.sessions.user_id IS 'Ссылка на пользователя';
COMMENT ON COLUMN users.sessions.access_token IS 'Краткосрочный токен (JWT)';
COMMENT ON COLUMN users.sessions.refresh_token IS 'Долгоживущий одноразовый токен';
COMMENT ON COLUMN users.sessions.last_login IS 'Дата и время последнего входа в систему';
COMMENT ON COLUMN users.sessions.useragent IS 'Строка useragent из заголовка запроса';
COMMENT ON COLUMN users.sessions.userip IS 'IP адрес, с которого был произведён вход';

ALTER TABLE core.users ADD COLUMN role_id BIGINT REFERENCES users.roles(id);
ALTER TABLE core.users ADD COLUMN smsCode int;