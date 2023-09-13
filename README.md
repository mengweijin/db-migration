# db-migration
<p align="center">
	<a target="_blank" href="https://github.com/mengweijin/flyway-extend/blob/master/LICENSE">
		<img src="https://img.shields.io/badge/license-Apache2.0-blue.svg" />
	</a>
	<a target="_blank" href="https://www.oracle.com/technetwork/java/javase/downloads/index.html">
		<img src="https://img.shields.io/badge/JDK-8+-green.svg" />
	</a>
	<a target="_blank" href="https://gitee.com/mengweijin/flyway-extend/stargazers">
		<img src="https://gitee.com/mengweijin/flyway-extend/badge/star.svg?theme=dark" alt='gitee star'/>
	</a>
	<a target="_blank" href='https://github.com/mengweijin/flyway-extend'>
		<img src="https://img.shields.io/github/stars/mengweijin/flyway-extend.svg?style=social" alt="github star"/>
	</a>
</p>

## 介绍
针对 Flyway、Liquibase 扩展官方默认不支持的一些数据库的支持。当前支持达梦（DM）数据库。

### Flyway 版本说明 	
flyway-extend 最新版本：
<a target="_blank" href="https://search.maven.org/search?q=g:%22com.github.mengweijin%22%20AND%20a:%22flyway-extend%22">
    <img src="https://img.shields.io/maven-central/v/com.github.mengweijin/flyway-extend" />
</a>

| flyway-extend |      flyway       |      spring boot       | 达梦（DM）数据库 |
|:-------------:|:-----------------:|:----------------------:|:---------:|
|   &#10006;    | flyway 版本 < 8.0.5 | spring boot 版本 < 2.6.2 | &#10006;  |
|     1.0.1     |       8.0.5       |      2.6.2(JDK 8)      | &#10004;  |
|     1.0.1     |      8.5.13       |      2.7.2(JDK 8)      | &#10004;  |
|     1.0.2     |       9.5.1       |     3.0.0(JDK 17)      | &#10004;  |
|     1.0.2     |      9.16.3       |     3.1.0(JDK 17)      | &#10004;  |

**注：达梦数据库请勿添加如下参数：comOracle=true&databaseProductName=Oracle&compatibleMode=oracle**

### Liquibase 版本说明
| liquibase-extend | liquibase | spring boot | 达梦（DM）数据库 |
|:----------------:|:---------:|:-----------:|:---------:|
|     &#10006;     |           |             | &#10006;  |
|      1.0.1       |           |             | &#10004;  |


**注：达梦数据库请勿添加如下参数：comOracle=true&databaseProductName=Oracle&compatibleMode=oracle**

### Flyway 使用
maven 引入 flyway-core 后，额外引入 flyway-extend。然后按照 Flyway 的使用方式直接使用即可。
```xml
<dependency>
    <groupId>com.github.mengweijin</groupId>
    <artifactId>flyway-extend</artifactId>
    <version>1.0.2</version>
</dependency>
```

#### Flyway MySQL 温馨提示
flyway-core 8.2.1及以后的版本不再直接支持 MySQL，需要额外引入：

官方说明：[https://flywaydb.org/documentation/database/mysql#maven](https://flywaydb.org/documentation/database/mysql#maven)
```xml
<dependency>
  <groupId>org.flywaydb</groupId>
  <artifactId>flyway-mysql</artifactId>
</dependency>
```

完整的基础使用示例参考各自 demo 工程。