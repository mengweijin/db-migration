## 使用 Flyway

### 达梦数据库
引入 db-migration-dm 的 maven 依赖。然后按照 Flyway 的使用方式直接使用即可。

**注意：** Flyway 达梦数据库**请勿**添加如下类似参数：**comOracle=true&databaseProductName=Oracle&compatibleMode=oracle**

```xml
<dependency>
    <groupId>com.github.mengweijin</groupId>
    <artifactId>db-migration-dm</artifactId>
    <version>${db-migration-dm.version}</version>
</dependency>
<dependency>
    <artifactId>flyway-core</artifactId>
    <groupId>org.flywaydb</groupId>
</dependency>
```

spring boot 参考配置：

```yaml
spring:
  profiles:
    active: local
  datasource:
    driver-class-name: dm.jdbc.driver.DmDriver
    # 勿添加如下类似参数：comOracle=true&databaseProductName=Oracle&compatibleMode=oracle
    url: jdbc:dm://localhost:5236
    username: SYSDBA
    password:
  flyway:
    # 默认不启用，true 为启用
    enabled: true
    baseline-on-migrate: true
    # baseline-version：产生的原因是兼容已经有版本发布的项目（即数据库中原本就存在一些表），要满足 3 个条件：
    # 1. baseline-on-migrate: true
    # 2. 数据库中已经存在其他表。
    # 3. flyway_schema_history 表不存在。
    # 当以上 3 个条件成立时，设置的 baseline-version 的值是多少，那么这个版本及之前版本的脚本都不会被执行。
    # 并且，flyway_schema_history 表中会多出第一条字段 script 为 << Flyway Baseline >> 的数据记录。
    # 不需要 baseline-version 的话可以注释掉。需要的话比如配置为：baseline-version: 2020.12.11
    baseline-version: 
    # 禁用 placeholder replacement，否则 sql 脚本中不能写 ${} 这样的字符。
    placeholder-replacement: false
    # flyway脚本命名规则为：V<VERSION>__<NAME>.sql (with <VERSION> an underscore-separated version, such as ‘1’ or ‘2_1’)
    # flyway在spring boot中默认配置位置为：classpath:db/migration
    locations:
      - classpath:db/migration/dm
      # - classpath:db/migration/h2
      # - classpath:db/migration/mysql
      # - classpath:db/migration/oracle
```

### Flyway MySQL / Oracle / PostgreSQL 等数据库温馨提示
flyway-core 8.2.1及以后的版本不再直接支持 MySQL，需要额外引入：

Flyway MySQL 官方文档：[https://documentation.red-gate.com/fd/mysql-184127601.html](https://documentation.red-gate.com/fd/mysql-184127601.html)
```xml
<dependency>
    <artifactId>flyway-core</artifactId>
    <groupId>org.flywaydb</groupId>
</dependency>
<dependency>
  <groupId>org.flywaydb</groupId>
  <artifactId>flyway-mysql</artifactId>
</dependency>
```

flyway-core 9.22.3 及以后的版本不再直接支持 Oracle(Flyway 9.16.3 还是直接支持 Oracle 的)，需要额外引入：

Flyway Oracle 官方文档：[https://documentation.red-gate.com/flyway/flyway-cli-and-api/supported-databases/oracle-database](https://documentation.red-gate.com/flyway/flyway-cli-and-api/supported-databases/oracle-database)
```xml
<dependency>
    <artifactId>flyway-core</artifactId>
    <groupId>org.flywaydb</groupId>
</dependency>
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-database-oracle</artifactId>
</dependency>
```

其他数据库类似，可参考官方文档（或者本仓库 demo 工程里已有的示例），这里不在赘述。

