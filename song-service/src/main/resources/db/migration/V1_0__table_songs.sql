-- table 'songs' >>> START
CREATE TABLE IF NOT EXISTS "songs"
(
    "id"          BIGSERIAL    NOT NULL PRIMARY KEY,
    "year"        SMALLINT     NULL,
    "name"        VARCHAR(100) NULL,
    "album"       VARCHAR(100) NULL,
    "artist"      VARCHAR(100) NULL,
    "length"      SMALLINT     NOT NULL,
    "resource_id" BIGINT       NOT NULL UNIQUE
) WITH (OIDS = FALSE);
-- table 'songs' <<< END
