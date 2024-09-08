## 使用 Liquibase

### 达梦数据库
引入 db-migration-dm 的 maven 依赖。然后按照 Liquibase 的官方使用方式使用即可。

**注意：** liquibase 基于 oracle 实现，因此 liquibase 中达梦数据库的 jdbc url 需要添加 **compatibleMode=oracle** 参数。

**比如：** jdbc:dm://localhost:5236?**compatibleMode=oracle**

```xml
<dependency>
    <groupId>com.github.mengweijin</groupId>
    <artifactId>db-migration-dm</artifactId>
    <version>${db-migration-dm.version}</version>
</dependency>
<dependency>
    <groupId>org.liquibase</groupId>
    <artifactId>liquibase-core</artifactId>
</dependency>
```

spring boot 配置参考：

```yaml
spring:
  profiles:
    active: local
  datasource:
    driver-class-name: dm.jdbc.driver.DmDriver
    url: jdbc:dm://localhost:5236?compatibleMode=oracle
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

### Liquibase + Flowable 使用
依赖、配置等参考 demo 目录下的 liquibase-dm 工程。注意 flowable 的配置项。

注意 flowable 版本:

* flowable 6.x.x 版本，适配 springboot 2.x；
* flowable 7.x.x 版本以及之后，适配 springboot 3.x；

#### 一、spring boot 配置：只用来初始化 flowable 相关的表

```yaml
spring:
  datasource:
    driver-class-name: dm.jdbc.driver.DmDriver
    url: jdbc:dm://localhost:5236?compatibleMode=oracle
    username: VTL_TEST
    password: 
flowable:
  # 一般和 spring.datasource.username 的值一样。
  database-schema: VTL_TEST
```

#### 二、spring boot 配置：初始化 flowable 相关的表，还要执行其他 sql 脚本

```yaml
spring:
  # 此处省略数据库相关配置，可参考上面一段配置。
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


