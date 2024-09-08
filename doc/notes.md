## 笔记

### 创建用户和授权

#### 达梦 与 Oracle
```shell
drop user VTL_TEST cascade;
create user VTL_TEST identified by "1qaz2wsx";
# grant 时注意和 oracle 的区别
# 达梦：
grant "PUBLIC","RESOURCE" to "VTL_TEST";
# oracle：
grant "CONNECT","RESOURCE" to "VTL_TEST";

grant "DBA" to "VTL_TEST";

# 修改密码
ALTER user VTL_TEST identified by "1qaz2wsx";
```

### 创建模式

```shell
# 给指定用户授予创建模式权限
grant create schema to <username>
# DBA用户或具有CREATE SCHEMA权限用户创建模式。
# AUTHORIZATION <username>标识了拥有该模式的用户；它是为其他用户创建模式时使用，缺省拥有该模式的用户为当前操作的用户（SYSDBA或是具有CREATE SCHEMA权限的用户）
create schema <schemaname> AUTHORIZATION <username>
# 或者
create schema <schemaname>
# 切换模式，从当前模式切换到 <schemaname> 模式
SET SCHEMA <schemaname>
# 模式删除语句
DROP SCHEMA <schemaname> [RESTRICT|CASCADE]
```

### 达梦备忘 SQL

```shell
SELECT * FROM ALL_USERS WHERE USERNAME = 'VTL_TEST';

SELECT * FROM ALL_OBJECTS WHERE OWNER='VTL_TEST';

SELECT DISTINCT OBJECT_NAME FROM ALL_OBJECTS WHERE OBJECT_TYPE = 'SCH' AND OBJECT_NAME = 'VTL_TEST';

```
