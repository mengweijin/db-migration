--liquibase formatted sql
--changeset admin:1 splitStatements:true
DROP TABLE IF EXISTS SYS_USER;

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

--liquibase formatted sql
--changeset admin:2 splitStatements:false
-- 基础版存储过程（返回单行版本信息）
CREATE OR REPLACE FUNCTION get_database_version()
RETURNS TEXT AS $$
BEGIN
    RETURN version(); -- 调用内置函数获取完整版本信息
END;
$$ LANGUAGE plpgsql;

SELECT get_database_version(); -- 调用

-- 测试第二个
CREATE OR REPLACE FUNCTION get_database_version2()
RETURNS TEXT AS $$
BEGIN
    RETURN version(); -- 调用内置函数获取完整版本信息
END;
$$ LANGUAGE plpgsql;

SELECT get_database_version2(); -- 调用