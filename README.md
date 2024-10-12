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

只需要引入包即可。如下：

```xml
<dependency>
    <groupId>com.github.mengweijin</groupId>
    <artifactId>db-migration</artifactId>
    <version>${db-migration.version}</version>
</dependency>

<!--flyway 或者 liquibase 用哪个引入哪个即可。-->

<!--flyway 的版本正常不需要指定，如果兼容 spring boot 2.5，则需要明确指定为 7.15.0 版本。-->
<dependency>
    <artifactId>flyway-core</artifactId>
    <groupId>org.flywaydb</groupId>
    <!--<version>10.10.0</version>-->
</dependency>

<!--liquibase 的版本正常不需要指定，也可以明确指定为 4.27.0 版本。-->
<dependency>
    <groupId>org.liquibase</groupId>
    <artifactId>liquibase-core</artifactId>
    <!--<version>4.27.0</version>-->
</dependency>
```

### db-migration 达梦（DM 8）

| db-migration | spring boot |    flyway | liquibase |
|:-------------|:------------|----------:|----------:|
| ——           | ——          |        —— |        —— |
| 2.0.1        | 2.4.x       |   7.1.1 ❌ |  4.27.0 ❌ |
| 2.0.1        | 2.5.x       |   7.7.3 ❌ |  4.27.0 ✅ |
| 2.0.1        | 2.5.x       |  7.15.0 ✅ |  4.27.0 ✅ |
| 2.0.1        | 2.6.x       |   8.0.4 ✅ |  4.27.0 ✅ |
| 2.0.1        | 2.7.x       |  8.5.11 ✅ |  4.27.0 ✅ |
| 2.0.1        | 3.0.x       |   9.5.1 ✅ |  4.27.0 ✅ |
| 2.0.1        | 3.1.x       |  9.16.3 ✅ |  4.27.0 ✅ |
| 2.0.1        | 3.2.x       |  9.22.3 ✅ |  4.27.0 ✅ |
| 2.0.1        | 3.3.x       | 10.10.0 ✅ |  4.27.0 ✅ |

### db-migration 南大通用（GBase 8s）

| db-migration | spring boot |    flyway | liquibase |
|:-------------|:------------|----------:|----------:|
| ——           | ——          |        —— |        —— |
| 2.0.1        | 2.4.x       |   7.1.1 ❌ |  4.27.0 ❌ |
| 2.0.1        | 2.5.x       |   7.7.3 ❌ |  4.27.0 ✅ |
| 2.0.1        | 2.5.x       |  7.15.0 ✅ |  4.27.0 ✅ |
| 2.0.1        | 2.6.x       |   8.0.4 ✅ |  4.27.0 ✅ |
| 2.0.1        | 2.7.x       |  8.5.11 ✅ |  4.27.0 ✅ |
| 2.0.1        | 3.0.x       |   9.5.1 ✅ |  4.27.0 ✅ |
| 2.0.1        | 3.1.x       |  9.16.3 ✅ |  4.27.0 ✅ |
| 2.0.1        | 3.2.x       |  9.22.3 ✅ |  4.27.0 ✅ |
| 2.0.1        | 3.3.x       | 10.10.0 ✅ |  4.27.0 ✅ |

## ⭐Star db-migration on GitHub

[![Stargazers over time](https://starchart.cc/mengweijin/db-migration.svg)](https://starchart.cc/mengweijin/db-migration)
