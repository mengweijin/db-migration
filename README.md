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
Flyway、Liquibase 扩展支持达梦（DM）数据库、南大通用（GBase 8s）数据库，并支持 Flowable 工作流。

**注意**：artifactId 已更新为 **db-migration**。使用旧版本 db-migration-dm 的需要更新为 db-migration。

### 已支持

* 达梦数据库（DM 8）。达梦使用 db-migration 时，默认支持 flowable 工作流。
* 南大通用数据库（GBase 8s）。南大通用使用 db-migration 时，需要额外引入 [db-migration-flowable](https://gitee.com/mengweijin/db-migration-flowable) 包来支持 flowable 工作流。

### 版本说明

* ❌❌：不支持；
* 🈯✅：flyway 或 liquibase **需要**指定特定版本才支持；
* ❄️✅：flyway 或 liquibase **不需要**指定版本就支持（不指定版本，则默认使用的 spring boot 默认版本）；

| db-migration 版本 | spring boot 版本 |   flyway 版本 | liquibase 版本 |
|:----------------|:---------------|------------:|-------------:|
| 2.0.7           | 2.0.x.RELEASE  |   7.15.0 ❌❌ |    4.27.0 ❌❌ |
| 2.0.7           | 2.1.x.RELEASE  |   7.15.0 ❌❌ |   4.27.0 🈯✅ | 
| 2.0.7           | 2.2.x.RELEASE  |   7.15.0 ❌❌ |   4.27.0 🈯✅ | 
| 2.0.7           | 2.3.x.RELEASE  |   7.15.0 ❌❌ |   4.27.0 🈯✅ | 
| 2.0.7           | 2.4.x          |  7.15.0 🈯✅ |   4.27.0 🈯✅ |  
| 2.0.7           | 2.5.x          |  7.15.0 🈯✅ |   4.27.0 🈯✅ |  
| 2.0.7           | 2.6.x          |   8.0.4 ❄️✅ |   4.27.0 🈯✅ | 
| 2.0.7           | 2.7.x          |  8.5.11 ❄️✅ |   4.27.0 🈯✅ | 
| 2.0.7           | 3.0.x          |   9.5.1 ❄️✅ |   4.27.0 🈯✅ | 
| 2.0.7           | 3.1.x          |  9.16.3 ❄️✅ |   4.27.0 🈯✅ | 
| 2.0.7           | 3.2.x          |  9.22.3 ❄️✅ |   4.27.0 🈯✅ | 
| 2.0.7           | 3.3.x          | 10.10.0 ❄️✅ |   4.27.0 ❄️✅ |
| 2.0.7           | 3.4.x          | 10.10.0 🈯✅ |   4.27.0 🈯✅ |

达梦要指定 schema 的话，直接在 jdbc url 中添加参数即可。比如：

jdbc:dm://localhost:5236?**schema=VTL_TEST_SCHEMA**

## db-migration-flowable 适配 Flowable 版本

注意：达梦数据库（DM 8）默认支持 flowable 工作流所有版本。所以不需要关注此章节。

此章节仅适用于以下数据库：

* 南大通用 GBase 8s 数据库

| Flowable 版本 | db-migration-flowable 版本 |        适配情况         |
|:------------|:-------------------------|:-------------------:|
| 6.8.0       | 6.8.0.0                  | 南大通用 GBase 8s 数据库：✅ |
| 7.0.1       | 7.0.1.1                  | 南大通用 GBase 8s 数据库：✅ |

Flowable 版本和 [db-migration-flowable](https://gitee.com/mengweijin/db-migration-flowable) 版本需要对应上。

比如：使用 flowable 7.0.1 版本的话，就要使用上面表格中对应的 db-migration-flowable 7.0.1.1 版本。

更多版本适配，请提 issue! 嘿嘿

## 参考文档

* [【达梦】 使用 Flyway](./doc/dm_use_flyway.md)
* [【达梦】 使用 Liquibase 和 Flowable 工作流](./doc/dm_use_liquibase_flowable.md)
* [【南大通用 GBase 8s】 使用 Flyway](./doc/gbase8s_use_flyway.md)
* [【南大通用 GBase 8s】 使用 Liquibase 和 Flowable 工作流](./doc/gbase8s_use_liquibase_flowable.md)

### Flyway 对 PL/SQL 的支持

Oracle 的存储过程、触发器、函数等 PL/SQL 代码块需以 `/` 符号结尾，告知 SQL 引擎执行该代码块。

因此，在 Flyway 中执行 Oracle 存储过程脚本时，必须在 PL/SQL 块的末尾添加 `/` 符号，以明确表示代码块的结束。
这是 Oracle 数据库对 PL/SQL 语法解析的要求，Flyway 在执行脚本时同样需要遵循这一规则。

Flyway 默认使用普通 SQL 解析器执行脚本，而 Oracle 的 PL/SQL 块需要明确的分隔符。添加 / 符号能帮助 Flyway 识别代码块边界。

例如在创建存储过程或触发器时：

```sql
CREATE OR REPLACE PROCEDURE test_proc AS
BEGIN
    -- 存储过程逻辑。此处略。
END;
/
   
CREATE TRIGGER test_trig AFTER INSERT ON test_user
BEGIN
    UPDATE test_user SET name = CONCAT(name, 'triggered');
END;
/
```

与普通 SQL 脚本的区别：

普通的 DDL（如建表）或 DML（如插入数据）脚本无需 / 符号，因为它们不是多行代码块。 例如：

```sql
CREATE TABLE users (id NUMBER PRIMARY KEY);
INSERT INTO users VALUES (1);
```

但涉及 PL/SQL 结构的脚本（如存储过程、函数、包）必须添加 /。

## 其他文档

* [Oracle 清理 Flowable 7.0.1 所有表脚本](./doc/use_oracle_flowable_drop_script.md)
* [MySQL、Oracle、PostgreSQL 等数据库使用Flyway 的温馨提示](./doc/z_flyway_supported_database_notes.md)

完整的基础使用示例参考代码仓库中，各自的 demo 工程。

## 重要👉：关于达梦 JDBC Driver 的坑！

达梦历史上 JDBC Driver 的 artifactId 发生过变化，并且有一些 BUG。如果是使用老版本的小伙伴，请切换为新版本。

```xml
<!-- 新版本 -->
<!-- 注意：artifactId 已变更为 DmJdbcDriver18 -->
<dependency>
    <groupId>com.dameng</groupId>
    <artifactId>DmJdbcDriver18</artifactId>
<!--<version>8.1.2.192</version>-->
<!--<version>8.1.3.140</version>-->
</dependency>

<!-- 旧版本 -->
<dependency>
    <groupId>com.dameng</groupId>
    <artifactId>Dm8JdbcDriver18</artifactId>
    <version>8.1.1.49</version>
</dependency>
```

## 捉急请联系我👇
|     QQ      |       邮箱        |
|:-----------:|:---------------:|
| 1002284406  | mwjwork@qq.com  |

## ⭐Star db-migration on GitHub

[![Stargazers over time](https://starchart.cc/mengweijin/db-migration.svg)](https://starchart.cc/mengweijin/db-migration)
