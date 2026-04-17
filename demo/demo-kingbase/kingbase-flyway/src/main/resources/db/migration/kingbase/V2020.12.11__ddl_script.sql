-- 查询是否为视图（kingbase 中默认已经有一个名为 SYS_USER 的试图了，测试时改为其它名字）
--SELECT * FROM sys_views WHERE viewname = 'SYS_USER';

-- 查询是否为表
--SELECT * FROM sys_tables WHERE tablename = 'SYS_USER';

DROP TABLE IF EXISTS VTL_SYS_USER;

CREATE TABLE VTL_SYS_USER (
    ID                              VARCHAR(36) NOT NULL,
    USERNAME                        VARCHAR(255) NULL,
    PASSWORD                        VARCHAR(255) NULL,
    CREATE_BY                       VARCHAR(255) NULL,
    CREATE_TIME                     TIMESTAMP(6) NULL,
    UPDATE_BY                       VARCHAR(255) NULL,
    UPDATE_TIME                     TIMESTAMP(6) NULL,
    PRIMARY KEY (ID)
);

insert into VTL_SYS_USER values('1', 'admin', '123456', 'SYSTEM', null, 'SYSTEM', null);

CREATE OR REPLACE FUNCTION get_database_version()
RETURNS TEXT AS $$
BEGIN
    RETURN version(); -- 调用内置函数获取完整版本信息
END;
$$ LANGUAGE plpgsql;

SELECT get_database_version(); -- 调用
