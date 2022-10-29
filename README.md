# Flyway-Extend
<p align="center">	
	<a target="_blank" href="https://search.maven.org/search?q=g:%22com.github.mengweijin%22%20AND%20a:%22flyway-extend%22">
		<img src="https://img.shields.io/maven-central/v/com.github.mengweijin/flyway-extend" />
	</a>
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
扩展添加对 Flyway 官方默认不支持的一些数据库的支持。

### 已支持：
- 达梦数据库（jdbc:dm://localhost:5236）
  - 请勿添加如下参数：comOracle=true&databaseProductName=Oracle&compatibleMode=oracle

### 版本说明
| flyway-extend | flyway | Spring Boot | 达梦数据库 |
|:-------------:|:------:|:-----------:|:-----:|
|     1.0.1     | 8.5.13 |    2.7.2    |   8   |


### 使用
maven 引入 flyway-core 后，额外引入 flyway-extend。然后按照 Flyway 的使用方式直接使用即可。
```xml
<dependency>
    <groupId>com.github.mengweijin</groupId>
    <artifactId>flyway-extend</artifactId>
    <version>${flyway-extend.version}</version>
</dependency>
```