-- table 'storages' >>> START
CREATE TABLE IF NOT EXISTS "storages"
(
    "id"     BIGSERIAL     NOT NULL PRIMARY KEY,
    "type"   VARCHAR(30)   NOT NULL,
    "name"   VARCHAR(1024) NOT NULL
) WITH (OIDS = FALSE);
-- table 'storages' <<< END
