# 达梦数据库使用 Liquibase

引入 db-migration 的 maven 依赖。然后按照 Liquibase 的官方使用方式使用即可。

```xml
<!--注意引入顺序，db-migration 必须在前面先引入。-->
<dependency>
    <groupId>com.github.mengweijin</groupId>
    <artifactId>db-migration</artifactId>
    <version>${db-migration.version}</version>
</dependency>
<!--liquibase 的版本一般不需要指定（会使用 spring boot 默认的版本），如果要兼容 spring boot 2.1 至 3.2 版本，则需要明确指定为 4.27.0 版本。-->
<dependency>
    <groupId>org.liquibase</groupId>
    <artifactId>liquibase-core</artifactId>
    <version>4.27.0</version>
</dependency>
```

spring boot 配置参考：

```yaml
spring:
  profiles:
    active: local
  datasource:
    driver-class-name: dm.jdbc.driver.DmDriver
    url: jdbc:dm://localhost:5236
    username: VTL_TEST
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

## 达梦数据库 + Liquibase + Flowable 使用

* flowable 6.x.x 版本，适配 springboot 2.x；
* flowable 7.x.x 版本以及之后，适配 springboot 3.x；

注意：application.yml 中 flowable.database-schema 的配置项。

### 一、只用来初始化 flowable 相关的表

```yaml
spring:
  datasource:
    driver-class-name: dm.jdbc.driver.DmDriver
    url: jdbc:dm://localhost:5236
    username: VTL_TEST
    password: 
flowable:
  # 一般和 spring.datasource.username 的值一样。
  database-schema: VTL_TEST
```

### 二、初始化 flowable 相关的表，还要执行其他 sql 脚本

```yaml
spring:
  datasource:
    driver-class-name: dm.jdbc.driver.DmDriver
    url: jdbc:dm://localhost:5236
    username: VTL_TEST
    password:
  liquibase:
    # 启用liquibase
    enabled: true
    # 存储变化的文件（changelog）位置。默认：classpath:/db/changelog/db.changelog-master.xml
    # 如果只用来初始化 flowable 相关的表，change-log 可以不用配置。
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
flowable:
  # 一般和 spring.datasource.username 的值一样。
  database-schema: VTL_TEST
```


