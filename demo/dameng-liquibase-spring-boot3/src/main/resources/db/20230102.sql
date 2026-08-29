--liquibase formatted sql
--changeset admin:5 splitStatements:true
CREATE TABLE SYS_USER_BAK (
    ID                              VARCHAR2(36) NOT NULL,
    USERNAME                        VARCHAR2(255) NULL,
    PASSWORD                        VARCHAR2(255) NULL,
    CREATE_BY                       VARCHAR2(255) NULL,
    CREATE_TIME                     TIMESTAMP(6) NULL,
    UPDATE_BY                       VARCHAR2(255) NULL,
    UPDATE_TIME                     TIMESTAMP(6) NULL,
    PRIMARY KEY (ID)
);

COMMENT ON TABLE SYS_USER_BAK                                IS '用户表';
COMMENT ON COLUMN SYS_USER_BAK.ID                            IS '主键ID';
COMMENT ON COLUMN SYS_USER_BAK.USERNAME                      IS '用户名';
COMMENT ON COLUMN SYS_USER_BAK.PASSWORD                      IS '密码';
COMMENT ON COLUMN SYS_USER_BAK.CREATE_BY                     IS '创建者';
COMMENT ON COLUMN SYS_USER_BAK.CREATE_TIME                   IS '创建时间';
COMMENT ON COLUMN SYS_USER_BAK.UPDATE_BY                     IS '更新者';
COMMENT ON COLUMN SYS_USER_BAK.UPDATE_TIME                   IS '更新时间';
