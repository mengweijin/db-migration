--liquibase formatted sql
--changeset admin:1 splitStatements:false
BEGIN
     EXECUTE IMMEDIATE 'DROP TABLE SYS_USER';
     EXCEPTION WHEN OTHERS THEN NULL;
END;

--changeset admin:2 splitStatements:false
declare
      num number;
begin
    select count(1) into num from user_tables where table_name = upper('SYS_USER');
    if num > 0 then
        execute immediate 'drop table SYS_USER';
    end if;
end;


--changeset admin:3 splitStatements:true
CREATE TABLE SYS_USER (
    ID                              VARCHAR2(36) NOT NULL,
    USERNAME                        VARCHAR2(255) NULL,
    PASSWORD                        VARCHAR2(255) NULL,
    CREATE_BY                       VARCHAR2(255) NULL,
    CREATE_TIME                     TIMESTAMP(6) NULL,
    UPDATE_BY                       VARCHAR2(255) NULL,
    UPDATE_TIME                     TIMESTAMP(6) NULL,
    PRIMARY KEY (ID)
);

COMMENT ON TABLE SYS_USER                                IS '用户表';
COMMENT ON COLUMN SYS_USER.ID                            IS '主键ID';
COMMENT ON COLUMN SYS_USER.USERNAME                      IS '用户名';
COMMENT ON COLUMN SYS_USER.PASSWORD                      IS '密码';
COMMENT ON COLUMN SYS_USER.CREATE_BY                     IS '创建者';
COMMENT ON COLUMN SYS_USER.CREATE_TIME                   IS '创建时间';
COMMENT ON COLUMN SYS_USER.UPDATE_BY                     IS '更新者';
COMMENT ON COLUMN SYS_USER.UPDATE_TIME                   IS '更新时间';

insert into SYS_USER values('1', 'admin', '123456', 'SYSTEM', sysdate, 'SYSTEM', sysdate);

