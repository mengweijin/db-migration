# GBase 8s 使用 Liquibase 和 Flowable

引入 db-migration 的 maven 依赖，然后按照 Liquibase 的官方使用方式使用即可。

```xml
<dependency>
    <groupId>com.github.mengweijin</groupId>
    <artifactId>db-migration</artifactId>
    <version>${db-migration.version}</version>
</dependency>
<!--liquibase 的版本一般不需要指定（会使用 spring boot 默认的版本），如果兼容 spring boot 2.1 至 2.5 版本，则需要明确指定为 4.5.0 版本。-->
<dependency>
    <groupId>org.liquibase</groupId>
    <artifactId>liquibase-core</artifactId>
    <!--<version>4.5.0</version>-->
</dependency>
```

spring boot 配置参考：

```yaml
spring:
  profiles:
    active: local
  datasource:
    driver-class-name: com.gbasedbt.jdbc.Driver
    url: jdbc:gbasedbt-sqli://localhost:9088/testdb:GBASEDBTSERVER=gbase01
    username: gbasedbt
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

## GBase 8s + Liquibase + Flowable 使用

注意 flowable 的配置项。

* flowable 6.x.x 版本，适配 springboot 2.x；
* flowable 7.x.x 版本以及之后，适配 springboot 3.x；

### 初始化 flowable 相关的表，并执行其他 sql 脚本

只需要额外引入对应版本的 [db-migration-flowable](https://gitee.com/mengweijin/db-migration-flowable) 包即可。

```xml
<dependency>
    <groupId>com.github.mengweijin</groupId>
    <artifactId>db-migration-flowable</artifactId>
    <version>${db-migration-flowable.version}</version>
</dependency>
```

注意：

Flowable 版本和 db-migration-flowable 版本需要对应上。

**db-migration-flowable** 版本适配情况参考：[db-migration-flowable 已适配 Flowable 版本](../README.md)