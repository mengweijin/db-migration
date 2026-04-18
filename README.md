<h1 align="center" style="margin: 0 0 30px 0">db-migration</h1>
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

<p align="center">
    Flyway、Liquibase 扩展支持达梦（DM）、南大通用（GBase 8s）、OpenGauss 等国产数据库。
</p>

## 数据库支持说明

|                数据库 | Flyway | Liquibase | 备注                                                        |
|-------------------:|:------:|:---------:|:----------------------------------------------------------|
|       **达梦（DM 8）** |   ✅    |     ✅     |                                                           |
| **南大通用（GBase 8s）** |   ✅    |     ✅     |                                                           |
|  **高斯（OpenGauss）** |   ✅    |    ❄️     | ❄️ 和 db-migration 无关，直接使用 PostgreSQL 即可。<br>写在这里避免有人混淆提问。 |
| **人大金仓（Kingbase）** |   ❄️   |    ❄️     | ❄️ 和 db-migration 无关，直接使用 PostgreSQL 即可。<br>写在这里避免有人混淆提问。 |

## db-migration 版本说明

- ❌：不支持；
- 🈯：flyway 或 liquibase **需要**指定特定版本才支持；
- ✅：flyway 或 liquibase **不需要**指定版本就支持（不指定版本，则默认使用的 spring boot 默认版本）；

| db-migration 版本 | spring boot 版本 |  flyway 版本 | liquibase 版本 |
|:----------------|:---------------|-----------:|-------------:|
| 2.2.1           | 2.0.x.RELEASE  |   7.15.0 ❌ |     4.27.0 ❌ |
| 2.2.1           | 2.1.x.RELEASE  |   7.15.0 ❌ |    4.27.0 🈯 | 
| 2.2.1           | 2.2.x.RELEASE  |   7.15.0 ❌ |    4.27.0 🈯 | 
| 2.2.1           | 2.3.x.RELEASE  |   7.15.0 ❌ |    4.27.0 🈯 | 
| 2.2.1           | 2.4.x          |  7.15.0 🈯 |    4.27.0 🈯 |  
| 2.2.1           | 2.5.x          |  7.15.0 🈯 |    4.27.0 🈯 |  
| 2.2.1           | 2.6.x          |    8.0.4 ✅ |    4.27.0 🈯 | 
| 2.2.1           | 2.7.x          |   8.5.11 ✅ |    4.27.0 🈯 | 
| 2.2.1           | 3.0.x          |    9.5.1 ✅ |    4.27.0 🈯 | 
| 2.2.1           | 3.1.x          |   9.16.3 ✅ |    4.27.0 🈯 | 
| 2.2.1           | 3.2.x          |   9.22.3 ✅ |    4.27.0 🈯 | 
| 2.2.1           | 3.3.x          |  10.10.0 ✅ |     4.27.0 ✅ |
| 2.2.1           | 3.4.x          | 10.10.0 🈯 |    4.27.0 🈯 |
| 2.2.1           | 3.5.x          | 10.10.0 🈯 |    4.27.0 🈯 |
| 计划中             | 4.0.x          |            |              |

## Maven 坐标

### 达梦 + Flyway

```xml
<dependency>
    <groupId>com.github.mengweijin</groupId>
    <artifactId>db-migration-dameng-flyway</artifactId>
    <version>${db-migration.version}</version>
</dependency>
```

### 达梦 + Liquibase

```xml
<dependency>
    <groupId>com.github.mengweijin</groupId>
    <artifactId>db-migration-dameng-liquibase</artifactId>
    <version>${db-migration.version}</version>
</dependency>
```

### 高斯 Open Gauss + Flyway

```xml
<dependency>
    <groupId>com.github.mengweijin</groupId>
    <artifactId>db-migration-gauss-flyway</artifactId>
    <version>${db-migration.version}</version>
</dependency>
```

### 南大通用 GBase 8s + Flyway

```xml
<dependency>
    <groupId>com.github.mengweijin</groupId>
    <artifactId>db-migration-gbase8s-flyway</artifactId>
    <version>${db-migration.version}</version>
</dependency>
```

### 南大通用 GBase 8s + Liquibase

```xml
<dependency>
    <groupId>com.github.mengweijin</groupId>
    <artifactId>db-migration-gbase8s-liquibase</artifactId>
    <version>${db-migration.version}</version>
</dependency>
```

## 使用文档

### 示例一：达梦数据库 + Flyway

```xml
<project>
    <properties>
        <!-- 锁定 flyway 版本号，低版本比如 spring boot 2.4.* 可对应修改为：7.15.0 -->
        <flyway.version>10.10.0</flyway.version>
        <!-- db-migration 版本（建议使用最新版本）-->
        <db-migration.version>2.2.1</db-migration.version>
    </properties>
    <dependencies>
        <!-- 引入要使用的 maven 坐标（其他数据库可引入对应的坐标） -->
        <dependency>
            <groupId>com.github.mengweijin</groupId>
            <artifactId>db-migration-dameng-flyway</artifactId>
            <version>${db-migration.version}</version>
        </dependency>
    </dependencies>
</project>
```

> 注意：
> 1. 使用 `flyway.version` 锁定 flyway 版本号（参考 `db-migration 版本说明` 章节）。
> 2. 引入要使用的 maven 坐标（其他数据库可引入对应的坐标）。
> 3. JDBC Driver 驱动包请自行引入。

然后按照 Flyway 原来的的使用方式使用即可。

其他`数据库 + Flyway` 的使用方式类似，不再赘述。


### 示例二：达梦数据库 + Liquibase

```xml
<project>
    <properties>
        <!-- 锁定 liquibase 版本号 -->
        <liquibase.version>4.27.0</liquibase.version>
        <!-- db-migration 版本（建议使用最新版本）-->
        <db-migration.version>2.2.1</db-migration.version>
    </properties>
    <dependencies>
        <!-- 引入要使用的 maven 坐标（其他数据库可引入对应的坐标） -->
        <dependency>
            <groupId>com.github.mengweijin</groupId>
            <artifactId>db-migration-dameng-liquibase</artifactId>
            <version>${db-migration.version}</version>
        </dependency>
    </dependencies>
</project>
```

> 注意：
> 1. 使用 `liquibase.version` 锁定 liquibase 版本号（参考 `db-migration 版本说明` 章节）。
> 2. 引入要使用的 maven 坐标（其他数据库可引入对应的坐标）。
> 3. JDBC Driver 驱动包请自行引入。

然后按照 Liquibase 原来的的使用方式使用即可。

其他`数据库 + Liquibase` 的使用方式类似，不再赘述。

## 示例工程

- [demo-dameng](demo/demo-dameng)

其他数据库的示例工程是旧的版本，还未及时更新。可参考达梦数据库示例工程 `demo-dameng`。 

## Flowable 说明

直接使用相关数据库的创建表脚本：

- [Flowable 6.8.1 版本数据库脚本](./flowable/6_8_1/)
- [Flowable 7.1.0 版本数据库脚本](./flowable/7_1_0/)

然后通过 Flyway 或 Liquibase 去执行这些脚本即可。

|                数据库 | Flowable 6.8.1 | Flowable 7.1.0 |
|-------------------:|:--------------:|:--------------:|
|       **达梦（DM 8）** |  使用 oracle 脚本  |  使用 oracle 脚本  |
| **南大通用（GBase 8s）** | 使用 gbase8s 脚本  |   暂无（欢迎 PR）    |
|  **高斯（OpenGauss）** | 使用 postgres 脚本 | 使用 postgres 脚本 |
| **人大金仓（Kingbase）** | 使用 postgres 脚本 | 使用 postgres 脚本 |

## 其它文档
- [Flyway 对 PL/SQL 的支持](./docs/z_flyway_supported_for_PL-SQL.md)
- [Flowable 6.8.1 清理所有表脚本](flowable/6_8_1/6.8.1.flowable.all.drop.sql)

## ⭐Star db-migration on GitHub

[![Stargazers over time](https://starchart.cc/mengweijin/db-migration.svg)](https://starchart.cc/mengweijin/db-migration)
