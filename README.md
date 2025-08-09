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
    <a href='https://gitee.com/mengweijin/db-migration/members'>
        <img src='https://gitee.com/mengweijin/db-migration/badge/fork.svg?theme=dark' alt='fork'>
    </a>
	<a target="_blank" href='https://github.com/mengweijin/db-migration'>
		<img src="https://img.shields.io/github/stars/mengweijin/db-migration?style=social" alt="github star"/>
	</a>
	<a target="_blank" href='https://github.com/mengweijin/db-migration'>
		<img src="https://img.shields.io/github/forks/mengweijin/db-migration?style=social" alt="github fork"/>
	</a>
</p>

## 介绍
Flyway、Liquibase 扩展支持达梦（DM）、南大通用（GBase 8s）、OpenGauss 等国产数据库。部分数据库直接支持 Flowable 工作流。

### 已支持

- **达梦（DM 8）**：支持 Flyway 和 Liquibase，支持 flowable 工作流。
- **南大通用（GBase 8s）**：支持 Flyway 和 Liquibase。
- **OpenGauss**：支持 Flyway，Liquibase 可直接使用 postgres 驱动得到支持。
- **人大金仓（Kingbase）**：可直接使用 postgres 驱动得到支持，无需依赖 db-migration 项目。

Flowable 的支持参考：[Flowable_脚本适配国产数据库及集成 Liquibase 或 Flyway 使用](./docs/z_flowable_sql_supported_database_in_china_local.md)

### 版本说明

- ❌❌：不支持；
- 🈯✅：flyway 或 liquibase **需要**指定特定版本才支持；
- ❄️✅：flyway 或 liquibase **不需要**指定版本就支持（不指定版本，则默认使用的 spring boot 默认版本）；

| db-migration 版本 | spring boot 版本 |   flyway 版本 | liquibase 版本 |
|:----------------|:---------------|------------:|-------------:|
| 2.0.8           | 2.0.x.RELEASE  |   7.15.0 ❌❌ |    4.27.0 ❌❌ |
| 2.0.8           | 2.1.x.RELEASE  |   7.15.0 ❌❌ |   4.27.0 🈯✅ | 
| 2.0.8           | 2.2.x.RELEASE  |   7.15.0 ❌❌ |   4.27.0 🈯✅ | 
| 2.0.8           | 2.3.x.RELEASE  |   7.15.0 ❌❌ |   4.27.0 🈯✅ | 
| 2.0.8           | 2.4.x          |  7.15.0 🈯✅ |   4.27.0 🈯✅ |  
| 2.0.8           | 2.5.x          |  7.15.0 🈯✅ |   4.27.0 🈯✅ |  
| 2.0.8           | 2.6.x          |   8.0.4 ❄️✅ |   4.27.0 🈯✅ | 
| 2.0.8           | 2.7.x          |  8.5.11 ❄️✅ |   4.27.0 🈯✅ | 
| 2.0.8           | 3.0.x          |   9.5.1 ❄️✅ |   4.27.0 🈯✅ | 
| 2.0.8           | 3.1.x          |  9.16.3 ❄️✅ |   4.27.0 🈯✅ | 
| 2.0.8           | 3.2.x          |  9.22.3 ❄️✅ |   4.27.0 🈯✅ | 
| 2.0.8           | 3.3.x          | 10.10.0 ❄️✅ |   4.27.0 ❄️✅ |
| 2.0.8           | 3.4.x          | 10.10.0 🈯✅ |   4.27.0 🈯✅ |

## 参考文档

- [【达梦 DM DBMS】 使用 Flyway](./docs/dm_use_flyway.md)
- [【达梦 DM DBMS】 使用 Liquibase 和 Flowable 工作流](./docs/dm_use_liquibase_flowable.md)
- [【南大通用 GBase 8s】 使用 Flyway](./docs/gbase8s_use_flyway.md)
- [【南大通用 GBase 8s】 使用 Liquibase](./docs/gbase8s_use_liquibase.md)
- [【华为 OpenGauss】 使用 Flyway](./docs/opengauss_use_flyway.md)
- [【华为 OpenGauss】 使用 Liquibase](./docs/opengauss_use_liquibase.md)
- [【人大金仓 Kingbase】 使用 Flyway 的示例工程](./demo-kingbase/kingbase-flyway)
- [【人大金仓 Kingbase】 使用 Liquibase 的示例工程](./demo-kingbase/kingbase-liquibase)

## 其它文档
- [Flowable_脚本适配国产数据库及集成 Liquibase 或 Flyway 使用](./docs/z_flowable_sql_supported_database_in_china_local.md)
- [Flyway 对 PL/SQL 的支持](./docs/z_flyway_supported_for_PL-SQL.md)
- [Flowable 6.8.1 清理所有表脚本](./flowable/6.8.1.flowable.all.drop.sql)
- [MySQL、Oracle、PostgreSQL 等数据库使用 Flyway 的温馨提示](./docs/z_flyway_supported_database_notes.md)

完整的基础使用示例参考代码仓库中，各自的 demo 工程。

## 捉急请联系我👇联系前请先提 issue!
|     QQ      |       邮箱        |
|:-----------:|:---------------:|
| 1002284406  | mwjwork@qq.com  |

## ⭐Star db-migration on GitHub

[![Stargazers over time](https://starchart.cc/mengweijin/db-migration.svg)](https://starchart.cc/mengweijin/db-migration)
