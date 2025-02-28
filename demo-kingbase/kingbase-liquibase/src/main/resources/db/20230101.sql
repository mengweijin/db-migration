--liquibase formatted sql
--changeset admin:1 splitStatements:true
-- 查询是否为视图（kingbase 中默认已经有一个名为 SYS_USER 的试图了，测试时改为其它名字）
--SELECT * FROM sys_views WHERE viewname = 'SYS_USER';

-- 查询是否为表
--SELECT * FROM sys_tables WHERE tablename = 'SYS_USER';

DROP TABLE IF EXISTS VTL_SYS_USER;

CREATE TABLE VTL_SYS_USER (
    ID                              VARCHAR2(36) NOT NULL,
    USERNAME                        VARCHAR2(255) NULL,
    PASSWORD                        VARCHAR2(255) NULL,
    CREATE_BY                       VARCHAR2(255) NULL,
    CREATE_TIME                     TIMESTAMP(6) NULL,
    UPDATE_BY                       VARCHAR2(255) NULL,
    UPDATE_TIME                     TIMESTAMP(6) NULL,
    PRIMARY KEY (ID)
);

COMMENT ON TABLE VTL_SYS_USER                                IS '用户表';
COMMENT ON COLUMN VTL_SYS_USER.ID                            IS '主键ID';
COMMENT ON COLUMN VTL_SYS_USER.USERNAME                      IS '用户名';
COMMENT ON COLUMN VTL_SYS_USER.PASSWORD                      IS '密码';
COMMENT ON COLUMN VTL_SYS_USER.CREATE_BY                     IS '创建者';
COMMENT ON COLUMN VTL_SYS_USER.CREATE_TIME                   IS '创建时间';
COMMENT ON COLUMN VTL_SYS_USER.UPDATE_BY                     IS '更新者';
COMMENT ON COLUMN VTL_SYS_USER.UPDATE_TIME                   IS '更新时间';

insert into VTL_SYS_USER values('1', 'admin', '123456', 'SYSTEM', sysdate, 'SYSTEM', sysdate);

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