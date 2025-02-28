## CHANGELOG
### [v2.0.8] 2025-03-01
- 【适配】增加 Flyway 对 OpenGauss 数据库的支持，以及增加 Liquibase 对 OpenGauss 数据库的使用示例。
- 【移除】移除南大通用 GBase8s 默认对 Flowable 的支持。需要的话建议从 Flowable 仓库拿到需要的版本的完整版 sql 脚本，有针对性的修改。
- 【示例】增加 Flyway 和 Liquibase 对 kingbase 人大金仓数据库的使用示例。

### [v2.0.7] 2025-02-07
- 【Fixed】修复 Gitee issue IBK64W，flyway 支持达梦的 DROP INDEX IF EXISTS 语法。

### [v2.0.6] 2025-01-23
- 【Fixed】修复 Gitee issue IBJH0F，在flyway执行含有DROP TABLE IF EXISTS类型的语句的时候，如果DROP前面有注释或者两个以上的换行符而抛出异常的问题。

### [v2.0.5] 2024-10-28
- 【优化】修复 liquibase 达梦指定 schema 时二次启动报错的问题。

### [v2.0.4] 2024-10-27
- 【优化】修改 liquibase 实现方式。

### [v2.0.3] 2024-10-19
- 【Gitee issue IAY19A】修复达梦数据库中 liquibase 第二次启动报错的问题。

### [v2.0.2] 2024-10-13
- 【优化】兼容达梦和南大通用 GBase 8s 数据库对 flyway 和 liquibase 低版本的支持。

### [v2.0.1] 2024-10-12
- 【优化】兼容达梦和南大通用 GBase 8s 数据库对 flyway 低版本的支持。

### [v2.0.0] 2024-10-12
- 【重构】重构达梦 flyway（version 10） 和 liquibase（version 4） 逻辑。
- 【新增】增加南大通用 GBase 8s 数据库 flyway（version 10）和（version 4）支持。
- 【修改 artifactId】artifactId 修改为 db-migration。

### [v1.1.9] 2024-09-10
- 【Gitee issue IAQ6CG】Fixed liquibase drop-first-true throw invalid table or view name error.

### [v1.1.8] 2024-09-08
- 【Github issue 7】Fixed flyway baseline-version not work.

### [v1.1.7] 2024-08-17
- 【适配】适配 spring boot 3.2.x 和 flyway 9.22.3 版本。

### [v1.1.6] 2024-05-18
- 【优化】修复 flyway 必须拥有 DBA 角色才能运行的问题。
- 【优化】修复 Liquibase 对 HikariProxyConnection 识别不准确导致控制台报 Cannot read from DBMS_UTILITY.DB_VERSION 的问题。

### [v1.1.5] 2024-05-18
- 【丢弃】一个 Liquibase 有点小问题的版本，flyway 不受影响。

### [v1.1.4] 2024-03-16
- 【优化】优化代码实现，修复对 Liquibase 的低版本兼容性问题。

### [v1.1.3] 2023-12-14
- 【适配】优化代码实现，修复 flyway 中 “CREATE TABLE IF NOT EXISTS XXX”语法解析异常

### [v1.1.2] 2023-12-06
- 【适配】优化代码实现，修复 flyway 中 DROP TABLE IF EXISTS xxx; 语句解析异常。

### [v1.1.1] 2023-12-05
- 【优化】优化代码实现，扩展 flyway 从 7.15.0 版本到 9.x（同时支持 Spring Boot 2.x 和 3.x）。

### [v1.1.0] 2023-09-16
- 【优化】支持 flyway、liquibase（Spring Boot 3.0.0+）。

### [v1.0.8] 2023-09-16
- 【修改 artifactId】artifactId 从 flyway-extend 修改为 db-migration-dm，并增加支持 liquibase（Spring Boot 2.6.0+）。

### [v1.0.2] 2023-09-13
- 【增强】支持 flwyway 9.5.1 ~ 9.16.3 版本（Spring Boot 3.0.0+）。

### [v1.0.1] 2022-10-29
- 【发布】第一次提交代码并发布 flyway-extend 初始版本。支持 flyway 8.0.5 ~ 8.5.13 版本（Spring Boot 2.6.0+）。





