-- table 'resources' >>> START
CREATE TABLE IF NOT EXISTS "resources"
(
    "id"       BIGSERIAL     NOT NULL PRIMARY KEY,
    "filename" VARCHAR(1024) NOT NULL UNIQUE
) WITH (OIDS = FALSE);
-- table 'resources' <<< END
