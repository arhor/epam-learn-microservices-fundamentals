-- table 'resources' >>> START
CREATE TABLE IF NOT EXISTS "resources"
(
    "id"       BIGSERIAL     NOT NULL PRIMARY KEY,
    "filename" VARCHAR(1024) NOT NULL UNIQUE,
    "status"   VARCHAR(10)   NOT NULL,
    "created"  TIMESTAMP     NOT NULL,
    "updated"  TIMESTAMP     NULL
) WITH (OIDS = FALSE);
-- table 'resources' <<< END
