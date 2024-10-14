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
Flyway、Liquibase 扩展支持达梦（DM）数据库、南大通用（GBase 8s）数据库。

只需要 maven 引入包即可（更多使用示例参考 demo 工程）。如下：

**注意**：artifactId 已更新为 **db-migration**。使用旧版本 db-migration-dm 的需要更新为 db-migration。

### 已支持

* 达梦数据库（DM 8），支持 flowable 工作流。
* 南大通用数据库（GBase 8s）

### 版本说明

* ❌❌：不支持；
* 🈯✅：flyway 或 liquibase **需要**指定特定版本才支持；
* ❄️✅：flyway 或 liquibase **不需要**指定版本就支持（不指定版本，则默认使用的 spring boot 默认版本）；

| db-migration 版本 | spring boot 版本 |   flyway 版本 | liquibase 版本 |
|:----------------|:---------------|------------:|-------------:|
| 2.0.2           | 2.0.x.RELEASE  |   7.15.0 ❌❌ |     4.5.0 ❌❌ |
| 2.0.2           | 2.1.x.RELEASE  |   7.15.0 ❌❌ |    4.5.0 🈯✅ |
| 2.0.2           | 2.2.x.RELEASE  |   7.15.0 ❌❌ |    4.5.0 🈯✅ |
| 2.0.2           | 2.3.x.RELEASE  |   7.15.0 ❌❌ |    4.5.0 🈯✅ |
| 2.0.2           | 2.4.x          |  7.15.0 🈯✅ |    4.5.0 🈯✅ |
| 2.0.2           | 2.5.x          |  7.15.0 🈯✅ |    4.5.0 🈯✅ |
| 2.0.2           | 2.6.x          |   8.0.4 ❄️✅ |    4.5.0 ❄️✅ |
| 2.0.2           | 2.7.x          |  8.5.11 ❄️✅ |    4.9.1 ❄️✅ |
| 2.0.2           | 3.0.x          |   9.5.1 ❄️✅ |   4.17.2 ❄️✅ |
| 2.0.2           | 3.1.x          |  9.16.3 ❄️✅ |   4.20.0 ❄️✅ |
| 2.0.2           | 3.2.x          |  9.22.3 ❄️✅ |   4.24.0 ❄️✅ |
| 2.0.2           | 3.3.x          | 10.10.0 ❄️✅ |   4.27.0 ❄️✅ |

## 参考文档

* [使用 flyway](./doc/use_flyway.md)
* [使用 liquibase + flowable 工作流](./doc/use_liquibase.md)
* [附录：清理 flowable 所有表脚本](./doc/use_flowable_drop_script.md)

完整的基础使用示例参考代码仓库中，各自的 demo 工程。

## ⭐Star db-migration on GitHub

[![Stargazers over time](https://starchart.cc/mengweijin/db-migration.svg)](https://starchart.cc/mengweijin/db-migration)
