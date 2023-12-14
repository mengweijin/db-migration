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
db-migration-dm 最新版本：
<a target="_blank" href="https://search.maven.org/search?q=g:%22com.github.mengweijin%22%20AND%20a:%22db-migration-dm%22">
    <img src="https://img.shields.io/maven-central/v/com.github.mengweijin/db-migration-dm" />
</a>

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
引入 maven 依赖。然后按照 Liquibase 的使用方式直接使用即可。

**注意：** liquibase 中达梦数据库的 jdbc url 需要添加 **compatibleMode=oracle** 参数。
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

#### Liquibase + Flowable 使用
依赖、配置等参考 demo 目录下的 liquibase-dm 工程。注意 flowable 的配置项。

完整的基础使用示例参考各自 demo 工程。

## ⭐Star Vitality

[![Stargazers over time](https://starchart.cc/mengweijin/db-migration.svg)](https://starchart.cc/mengweijin/db-migration)
