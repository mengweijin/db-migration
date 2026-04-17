DROP TABLE IF EXISTS SYS_USER_BASELINE;

CREATE TABLE SYS_USER_BASELINE (
    ID                              VARCHAR(36) NOT NULL,
    USERNAME                        VARCHAR(255) NULL,
    PASSWORD                        VARCHAR(255) NULL,
    CREATE_BY                       VARCHAR(255) NULL,
    CREATE_TIME                     TIMESTAMP(6) NULL,
    UPDATE_BY                       VARCHAR(255) NULL,
    UPDATE_TIME                     TIMESTAMP(6) NULL,
    PRIMARY KEY (ID)
);
