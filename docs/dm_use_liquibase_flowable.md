# 达梦数据库使用 Liquibase

引入 db-migration 的 maven 依赖。然后按照 Liquibase 的官方使用方式使用即可。

达梦要指定 schema 的话，直接在 jdbc url 中添加参数即可。比如：

jdbc:dm://localhost:5236?**schema=VTL_TEST_SCHEMA**

```xml
<!--注意引入顺序，db-migration 必须在前面先引入。-->
<dependency>
    <groupId>com.github.mengweijin</groupId>
    <artifactId>db-migration</artifactId>
    <version>${db-migration.version}</version>
</dependency>
<!--liquibase 的版本固定使用 4.27.0 -->
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

提示：若要指定 schema，可修改 datasource 中的 url。比如：jdbc:dm://localhost:5236?schema=VTL_TEST_SCHEMA

## 和 Flowable 一起使用

引入 flowable 依赖即可。

```xml
<dependency>
    <groupId>org.flowable</groupId>
    <artifactId>flowable-spring-boot-starter-process</artifactId>
    <version>${flowable.version}</version>
</dependency>
```

application.yml 中按实际需求增加 flowable 配置：

```yaml
flowable:
  async-executor-activate: false
  database-schema-update: true
  database-schema: VTL_TEST
```

* flowable 6.x.x 版本，适配 springboot 2.x；
* flowable 7.x.x 版本以及之后，适配 springboot 3.x；

## 重要👉：关于达梦 JDBC Driver 的坑！

达梦历史上 JDBC Driver 的 artifactId 发生过变化，并且有一些 BUG。如果是使用老版本的小伙伴，请切换为新版本。

```xml
<!-- 新版本 -->
<!-- 注意：artifactId 已变更为 DmJdbcDriver18 -->
<dependency>
    <groupId>com.dameng</groupId>
    <artifactId>DmJdbcDriver18</artifactId>
<!--<version>8.1.2.192</version>-->
<!--<version>8.1.3.140</version>-->
</dependency>

<!-- 旧版本 -->
<dependency>
    <groupId>com.dameng</groupId>
    <artifactId>Dm8JdbcDriver18</artifactId>
    <version>8.1.1.49</version>
</dependency>
```