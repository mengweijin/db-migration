## CHANGELOG
### [v2.0.0] 2024-10-12
- 【重构】重构达梦 flyway（version 10） 和 liquibase（version 4） 逻辑。
- 【新增】增加南大通用 GBase 8s 数据库 flyway（version 10）和（version 4）支持。

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





