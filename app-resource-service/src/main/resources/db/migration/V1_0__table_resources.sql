-- table 'resources' >>> START
CREATE TABLE IF NOT EXISTS "resources"
(
    "id"         BIGSERIAL     NOT NULL PRIMARY KEY,
    "filename"   VARCHAR(1024) NOT NULL UNIQUE,
    "storage_id" BIGINT        NOT NULL,

    CONSTRAINT UQ_filename_storage_id UNIQUE ("filename", "storage_id")
) WITH (OIDS = FALSE);
-- table 'resources' <<< END
