# db-migration
<p align="center">
    <a target="_blank" href="https://search.maven.org/search?q=g:%22com.github.mengweijin%22%20AND%20a:%22db-migration%22">
        <img src="https://img.shields.io/maven-central/v/com.github.mengweijin/db-migration?label=db-migration&color=blue" />
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
针对 Flyway、Liquibase 扩展支持达梦（DM）数据库、南大通用（GBase 8s）数据库。

只需要 maven 引入包即可。如下：

```xml
<dependency>
    <groupId>com.github.mengweijin</groupId>
    <artifactId>db-migration</artifactId>
    <version>${db-migration.version}</version>
</dependency>

<!--flyway 或者 liquibase 用哪个引入哪个即可。-->

<!--flyway 的版本一般不需要指定，如果兼容 spring boot 2.5 和 2.4 版本，则需要明确指定为 7.15.0 版本。-->
<dependency>
    <artifactId>flyway-core</artifactId>
    <groupId>org.flywaydb</groupId>
    <!--<version>7.15.0</version>-->
</dependency>

<!--liquibase 的版本一般不需要指定，，如果兼容 spring boot 2.1 至 2.5 版本，则需要明确指定为 4.5.0 版本。-->
<dependency>
    <groupId>org.liquibase</groupId>
    <artifactId>liquibase-core</artifactId>
    <!--<version>4.5.0</version>-->
</dependency>
```

## db-migration：达梦（DM 8） 和 南大通用（GBase 8s）

| db-migration | spring boot   |       flyway |   liquibase |
|:-------------|:--------------|-------------:|------------:|
| 2.0.2        | 2.0.x.RELEASE |    7.15.0 ❌❌ |    4.5.0 ❌❌ |
| 2.0.2        | 2.1.x.RELEASE |    7.15.0 ❌❌ |   4.5.0 🈯✅ |
| 2.0.2        | 2.2.x.RELEASE |    7.15.0 ❌❌ |   4.5.0 🈯✅ |
| 2.0.2        | 2.3.x.RELEASE |    7.15.0 ❌❌ |   4.5.0 🈯✅ |
| 2.0.2        | 2.4.x         |   7.15.0 🈯✅ |   4.5.0 🈯✅ |
| 2.0.2        | 2.5.x         |   7.15.0 🈯✅ |   4.5.0 🈯✅ |
| 2.0.2        | 2.6.x         |   8.0.4 *️⃣✅ |  4.5.0 *️⃣✅ |
| 2.0.2        | 2.7.x         |  8.5.11 *️⃣✅ |  4.9.1 *️⃣✅ |
| 2.0.2        | 3.0.x         |   9.5.1 *️⃣✅ | 4.27.0 *️⃣✅ |
| 2.0.2        | 3.1.x         |  9.16.3 *️⃣✅ | 4.20.0 *️⃣✅ |
| 2.0.2        | 3.2.x         |  9.22.3 *️⃣✅ | 4.24.0 *️⃣✅ |
| 2.0.2        | 3.3.x         | 10.10.0 *️⃣✅ | 4.27.0 *️⃣✅ |

### 达梦 liquibase 支持 flowable 工作流

* flowable 6.x.x 版本，适配 springboot 2.x；
* flowable 7.x.x 版本以及之后，适配 springboot 3.x；

#### 一、只用来初始化 flowable 相关表

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

#### 二、初始化 flowable 相关的表，还要执行其他 sql 脚本

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


## ⭐Star db-migration on GitHub

[![Stargazers over time](https://starchart.cc/mengweijin/db-migration.svg)](https://starchart.cc/mengweijin/db-migration)
