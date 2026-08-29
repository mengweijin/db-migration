DROP TABLE IF EXISTS SYS_USER;

CREATE TABLE SYS_USER (
    ID                              VARCHAR(36) NOT NULL,
    USERNAME                        VARCHAR(255) NULL,
    PASSWORD                        VARCHAR(255) NULL,
    CREATE_BY                       VARCHAR(255) NULL,
    CREATE_TIME                     TIMESTAMP(6) NULL,
    UPDATE_BY                       VARCHAR(255) NULL,
    UPDATE_TIME                     TIMESTAMP(6) NULL,
    PRIMARY KEY (ID)
);

insert into SYS_USER values('1', 'admin', '123456', 'SYSTEM', null, 'SYSTEM', null);