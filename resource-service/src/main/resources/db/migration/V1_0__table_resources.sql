-- table 'resources' >>> START
CREATE TABLE IF NOT EXISTS "resources"
(
    "id"       BIGSERIAL     NOT NULL PRIMARY KEY,
    "filename" VARCHAR(1024) NOT NULL UNIQUE,
    "uploaded" BOOLEAN       NOT NULL,
    "length"   BIGINT        NOT NULL DEFAULT 0
) WITH (OIDS = FALSE);
-- table 'resources' <<< END
