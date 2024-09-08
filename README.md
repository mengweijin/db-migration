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
|    &#10006;     | spring boot 版本 < 2.6.2 |      版本 < 7.15.0      |   版本 <= 4.5.0   |
|  1.1.6 ~ 1.1.7  |                        | 版本 >= 7.15.0 &#10004; |                 |
|  1.1.6 ~ 1.1.7  |         2.6.x          |    8.0.5 &#10004;     | 4.5.0 &#10006;  |
|  1.1.6 ~ 1.1.7  |         2.7.x          |    8.5.13 &#10004;    | 4.9.1 &#10004;  |
|  1.1.6 ~ 1.1.7  |         3.0.x          |    9.5.1 &#10004;     | 4.17.2 &#10004; |
|  1.1.6 ~ 1.1.7  |         3.1.x          |    9.16.3 &#10004;    | 4.20.0 &#10004; |
|      1.1.6      |         3.2.x          |    9.22.3 &#10006;    | 4.24.0 &#10004; |
|      1.1.7      |         3.2.x          |    9.22.3 &#10004;    | 4.24.0 &#10004; |

* [文档：达梦数据库使用 flyway](./doc/flyway.md)
* [文档：达梦数据库使用 liquibase + flowable 工作流](./doc/liquibase.md)

完整的基础使用示例参考代码仓库中，各自的 demo 工程。

提示：在url中指定schema的方式：jdbc:dm://localhost:5236?schema=VTL_TEST

## ⭐Star db-migration

[![Stargazers over time](https://starchart.cc/mengweijin/db-migration.svg)](https://starchart.cc/mengweijin/db-migration)
