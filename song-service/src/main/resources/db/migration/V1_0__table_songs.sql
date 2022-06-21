-- table 'songs' >>> START
CREATE TABLE IF NOT EXISTS "songs"
(
    "id"          BIGSERIAL    NOT NULL PRIMARY KEY,
    "name"        VARCHAR(100) NULL,
    "year"        VARCHAR(4)   NULL,
    "album"       VARCHAR(100) NULL,
    "artist"      VARCHAR(100) NULL,
    "length"      VARCHAR(100) NULL,
    "resource_id" BIGINT       NOT NULL UNIQUE
) WITH (OIDS = FALSE);
-- table 'songs' <<< END
