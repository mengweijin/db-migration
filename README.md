# db-migration
<p align="center">
    <a target="_blank" href="https://github.com/mengweijin/db-migration">
		<img src="https://img.shields.io/badge/repo-Github-purple" />
	</a>
    <a target="_blank" href="https://gitee.com/mengweijin/db-migration">
		<img src="https://img.shields.io/badge/repo-码云 Gitee-purple" />
	</a>
    <a target="_blank" href="https://central.sonatype.com/artifact/com.github.mengweijin/db-migration/versions">
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
Flyway、Liquibase 扩展支持达梦（DM）、南大通用（GBase 8s）、OpenGauss 等国产数据库。

### 数据库支持说明

|                数据库 |      Flyway      |    Liquibase     | 备注                                    |
|-------------------:|:----------------:|:----------------:|:--------------------------------------|
|       **达梦（DM 8）** |        ✅         |        ✅         |                                       |
| **南大通用（GBase 8s）** |        ✅         |        ✅         |                                       |
|  **高斯（OpenGauss）** |        ✅         | 使用 PostgreSQL 驱动 | 使用 Liquibase 时，和 db-migration 无关，无需使用 |
| **人大金仓（Kingbase）** | 使用 PostgreSQL 驱动 | 使用 PostgreSQL 驱动 | 和 db-migration 无关，无需使用，这里仅提供使用信息      |

### db-migration 版本说明

- ❌❌：不支持；
- 🈯✅：flyway 或 liquibase **需要**指定特定版本才支持；
- ❄️✅：flyway 或 liquibase **不需要**指定版本就支持（不指定版本，则默认使用的 spring boot 默认版本）；

| db-migration 版本 | spring boot 版本 |   flyway 版本 | liquibase 版本 |
|:----------------|:---------------|------------:|-------------:|
| ❌❌              | 2.0.x.RELEASE  |   7.15.0 ❌❌ |    4.27.0 ❌❌ |
| 2.1.0           | 2.1.x.RELEASE  |   7.15.0 ❌❌ |   4.27.0 🈯✅ | 
| 2.1.0           | 2.2.x.RELEASE  |   7.15.0 ❌❌ |   4.27.0 🈯✅ | 
| 2.1.0           | 2.3.x.RELEASE  |   7.15.0 ❌❌ |   4.27.0 🈯✅ | 
| 2.1.0           | 2.4.x          |  7.15.0 🈯✅ |   4.27.0 🈯✅ |  
| 2.1.0           | 2.5.x          |  7.15.0 🈯✅ |   4.27.0 🈯✅ |  
| 2.1.0           | 2.6.x          |   8.0.4 ❄️✅ |   4.27.0 🈯✅ | 
| 2.1.0           | 2.7.x          |  8.5.11 ❄️✅ |   4.27.0 🈯✅ | 
| 2.1.0           | 3.0.x          |   9.5.1 ❄️✅ |   4.27.0 🈯✅ | 
| 2.1.0           | 3.1.x          |  9.16.3 ❄️✅ |   4.27.0 🈯✅ | 
| 2.1.0           | 3.2.x          |  9.22.3 ❄️✅ |   4.27.0 🈯✅ | 
| 2.1.0           | 3.3.x          | 10.10.0 ❄️✅ |   4.27.0 ❄️✅ |
| 2.1.0           | 3.4.x          | 10.10.0 🈯✅ |   4.27.0 🈯✅ |
| 2.1.0           | 3.5.x          | 10.10.0 🈯✅ |   4.27.0 🈯✅ |
| 计划中             | 4.0.x          |             |              |


### Flowable 支持说明

**注意！！！**：自 **2.1.0** 版本及以后，移除了达梦数据库默认对 flowable 支持的相关代码。后续如何使用 Flowable 参考如下：

可直接使用相关数据库的创建表脚本：

- [Flowable 6.8.1 版本数据库脚本](./flowable/6_8_1/)
- [Flowable 7.1.0 版本数据库脚本](./flowable/7_1_0/)

然后通过 Flyway 或 Liquibase 去执行这些脚本即可。

|                数据库 | Flowable 6.8.1 | Flowable 7.1.0 |
|-------------------:|:--------------:|:--------------:|
|       **达梦（DM 8）** |  使用 oracle 脚本  |  使用 oracle 脚本  |
| **南大通用（GBase 8s）** | 使用 gbase8s 脚本  |   暂无（欢迎 PR）    |
|  **高斯（OpenGauss）** | 使用 postgres 脚本 | 使用 postgres 脚本 |
| **人大金仓（Kingbase）** | 使用 postgres 脚本 | 使用 postgres 脚本 |

Flowable 相关示例工程：

- 达梦使用 **Flyway** + Flowable：[/demo-dm/dm-flyway-flowable](./demo-dm/dm-flyway-flowable)
- 达梦使用 **Liquibase** + Flowable：[/demo-dm/dm-liquibase-flowable](./demo-dm/dm-liquibase-flowable)

## 参考文档

- [【达梦 DM】 使用 Flyway](./docs/dm_use_flyway.md)
- [【达梦 DM】 使用 Liquibase](./docs/dm_use_liquibase.md)
- [【南大通用 GBase 8s】 使用 Flyway](./docs/gbase8s_use_flyway.md)
- [【南大通用 GBase 8s】 使用 Liquibase](./docs/gbase8s_use_liquibase.md)
- [【华为 OpenGauss】 使用 Flyway](./docs/opengauss_use_flyway.md)
- [【华为 OpenGauss】 使用 Liquibase](./docs/opengauss_use_liquibase.md)

## 其它文档
- [Flyway 对 PL/SQL 的支持](./docs/z_flyway_supported_for_PL-SQL.md)
- [Flowable 6.8.1 清理所有表脚本](flowable/6_8_1/6.8.1.flowable.all.drop.sql)
- [MySQL、Oracle、PostgreSQL 等数据库使用 Flyway 的温馨提示](./docs/z_flyway_supported_database_notes.md)

完整的基础使用示例参考代码仓库中，各自的 demo 工程。

## ⭐Star db-migration on GitHub

[![Stargazers over time](https://starchart.cc/mengweijin/db-migration.svg)](https://starchart.cc/mengweijin/db-migration)
