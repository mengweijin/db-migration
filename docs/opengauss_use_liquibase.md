# OpenGauss 数据库使用 Liquibase

Liquibase 可以直接使用 postgresql 的驱动包。然后按照 Liquibase 的官方使用方式使用即可。

因此，这里仅仅给出一个使用示例。如果想使用 Flyway 的话，参考 README.md 中的文档：【华为 OpenGauss】 使用 Flyway

```xml
<dependency>
    <groupId>org.liquibase</groupId>
    <artifactId>liquibase-core</artifactId>
</dependency>
<dependency>
<groupId>org.postgresql</groupId>
<artifactId>postgresql</artifactId>
<scope>runtime</scope>
</dependency>
```

spring boot 配置参考：

```yaml
spring:
  profiles:
    active: local
  datasource:
    driver-class-name: org.postgresql.Driver
    url: jdbc:postgresql://localhost:5432/postgres?binaryTransfer=false&forceBinary=false&reWriteBatchedInserts=true
    username:
    password:
  liquibase:
    # 启用liquibase
    enabled: true
    # 存储变化的文件（changelog）位置。默认：classpath:/db/changelog/db.changelog-master.xml
    change-log: classpath:/db/changelog.xml
    # 分环境执行，若在 changelog 文件中设置了对应 context 属性，则只会执行与 dev 对应值的 changeset
    # contexts: dev
    # 执行前首先删除数据库，默认 false。若设置为 true，则执行变更前，会先删除目标数据库，请谨慎
    # dropFirst: false
    # 执行更新时将回滚 SQL 写入的文件路径
    # rollback-file:
    # 如果使用工程已配置的 datasource 数据源，则以下三个数据库连接参数可不配置
    # 访问数据库的连接地址
    # url:
    # 访问数据库的用户名
    # user:
    # 访问数据库的密码
    # password:
```

## 多行代码块使用示例

```sql
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
```