# db-migration
<p align="center">
    <a target="_blank" href="https://search.maven.org/search?q=g:%22com.github.mengweijin%22%20AND%20a:%22db-migration-dm%22">
        <img src="https://img.shields.io/maven-central/v/com.github.mengweijin/db-migration-dm" />
    </a>
	<a target="_blank" href="https://github.com/mengweijin/db-migration/blob/master/LICENSE">
		<img src="https://img.shields.io/badge/license-Apache2.0-blue.svg" />
	</a>
	<a target="_blank" href="https://www.oracle.com/technetwork/java/javase/downloads/index.html">
		<img src="https://img.shields.io/badge/JDK-8+-green.svg" />
	</a>
	<a target="_blank" href="https://gitee.com/mengweijin/db-migration/stargazers">
		<img src="https://gitee.com/mengweijin/db-migration/badge/star.svg?theme=dark" alt='gitee star'/>
	</a>
	<a target="_blank" href='https://github.com/mengweijin/db-migration'>
		<img src="https://img.shields.io/github/stars/mengweijin/db-migration.svg?style=social" alt="github star"/>
	</a>
</p>

## 介绍
针对 Flyway、Liquibase 扩展官方默认不支持的一些数据库的支持。当前支持达梦（DM）数据库。

### db-migration-dm 版本说明（达梦）
| db-migration-dm |      spring boot       |        flyway         |    liquibase    |
|:---------------:|:----------------------:|:---------------------:|:---------------:|
|    &#10006;     | spring boot 版本 < 2.6.2 |      版本 < 7.15.0      | 版本 < 4.5.0(未测试) |
|      1.1.3      |                        | 版本 >= 7.15.0 &#10004; |                 |
|      1.1.3      |         2.6.x          |    8.0.5 &#10004;     |   4.5.0(未测试)    |
|      1.1.3      |         2.7.x          |    8.5.13 &#10004;    | 4.9.1 &#10004;  |
|      1.1.3      |         3.0.x          |    9.5.1 &#10004;     | 4.17.2 &#10004; |
|      1.1.3      |         3.1.x          |    9.16.3 &#10004;    | 4.20.0 &#10004; |

提示：在url中指定schema的方式：jdbc:dm://localhost:5236?schema=VTL_TEST&compatibleMode=oracle

### Flyway 使用（以达梦(DM)数据库为例）
引入 maven 依赖。然后按照 Flyway 的使用方式直接使用即可。

**注意：** Flyway 达梦数据库**请勿**添加如下参数：**comOracle=true&databaseProductName=Oracle&compatibleMode=oracle**

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

#### Flyway MySQL 温馨提示
flyway-core 8.2.1及以后的版本不再直接支持 MySQL，需要额外引入：

官方说明：[https://flywaydb.org/documentation/database/mysql#maven](https://flywaydb.org/documentation/database/mysql#maven)
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

### Liquibase 使用（以达梦(DM)数据库为例）
引入 maven 依赖。然后按照 Liquibase 的官方使用方式使用即可。

**注意：** liquibase 中达梦数据库的 jdbc url 需要添加 **compatibleMode=oracle** 参数。

比如：jdbc:dm://localhost:5236?compatibleMode=oracle
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

### Liquibase + Flowable 使用
依赖、配置等参考 demo 目录下的 liquibase-dm 工程。注意 flowable 的配置项。

#### flowable 参考配置
如果只用来初始化 flowable 相关的表，则参考如下配置：
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

#### liquibase 其他配置
如果不光要初始化 flowable 相关的表，还要额外执行其他 sql 脚本来初始化数据库，则参考：
```yaml
spring:
  # 此处省略数据库相关配置，可参考上面一段配置。
  liquibase:
    # 启用liquibase，Spring Boot 默认就是 true
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

完整的基础使用示例参考各自 demo 工程。

## ⭐Star db-migration

[![Stargazers over time](https://starchart.cc/mengweijin/db-migration.svg)](https://starchart.cc/mengweijin/db-migration)
