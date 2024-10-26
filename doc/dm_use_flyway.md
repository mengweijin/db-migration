# 达梦数据库使用 Flyway

引入 db-migration 的 maven 依赖。然后按照 Flyway 的使用方式直接使用即可。

```xml
<!--注意引入顺序，db-migration 必须在前面先引入。-->
<dependency>
    <groupId>com.github.mengweijin</groupId>
    <artifactId>db-migration</artifactId>
    <version>${db-migration.version}</version>
</dependency>
<!--flyway 的版本一般不需要指定（会使用 spring boot 默认的版本），如果兼容 spring boot 2.5 和 2.4 版本，则需要明确指定为 7.15.0 版本。-->
<dependency>
    <artifactId>flyway-core</artifactId>
    <groupId>org.flywaydb</groupId>
    <!--<version>7.15.0</version>-->
</dependency>
```

spring boot 参考配置：

```yaml
spring:
  profiles:
    active: local
  datasource:
    driver-class-name: dm.jdbc.driver.DmDriver
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
