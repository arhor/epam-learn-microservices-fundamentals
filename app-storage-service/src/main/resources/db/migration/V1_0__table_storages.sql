-- table 'storages' >>> START
CREATE TABLE IF NOT EXISTS "storages"
(
    "id"     BIGSERIAL     NOT NULL PRIMARY KEY,
    "name"   VARCHAR(1024) NOT NULL,
    "type"   VARCHAR(30)   NOT NULL,

    CONSTRAINT UQ_name_type UNIQUE ("name", "type")
) WITH (OIDS = FALSE);
-- table 'storages' <<< END
