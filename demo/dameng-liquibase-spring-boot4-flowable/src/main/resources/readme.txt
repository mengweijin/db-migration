classpath:/db/changelog.xml
xml中引用的 sql 文件需要按照格式编写。

参考：https://docs.liquibase.com/concepts/changelogs/sql-format.html

20230101.sql 基本规则：

1、第一行以固定格式开头：--liquibase formatted sql

2、第二行标记格式，如：--changeset admin:1 splitStatements:false
    2.1、格式参考：--changeset author:id attribute1:value1 attribute2:value2 [...]
    2.2、其中 author:id 表示【用户:id】，author 用来标记是那个用户维护的，id 可以采用从 1 自增开始或者按照日期自增，不能重复。
    2.3、splitStatements默认值为true，表示 sql 文件中默认以应为你分号 “;” 来分隔多个sql，分号也可以修改，比如：endDelimiter:$$。
    2.4、但是在某些情况下，不能分隔，比如 BEGIN...END 中间使用，就需要改为 false。
3、每一行 --changeset 会在 liquibase 记录表 DATABASECHANGELOG 中单独生成一条记录。

