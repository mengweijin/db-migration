## 使用 flowable（使用的Liquibase）同时使用 flyway来管理自己的 SQL的一点点疑问

使用情景：项目用到了 flowable 工作流，flowable 又用到了 liquibase，但又想拿 flyway 来管理 sql 脚本。

已知，db-migration-dm 中：

* 使用 liquibase 时，jdbc url 中**需要**添加 &compatibleMode=oracle 参数；
* 使用 flyway 时，jdbc url 中**不能**添加 &compatibleMode=oracle 参数；

那么问题来了，在这种场景下，一个要加，一个不加，spring boot 中 datasource 的配置就矛盾了，咋整？


可使用如下解决方案：

### 方案一
统一使用 Liquibase 来管理 flowable 和 自己工程的 sql 脚本，这样就没冲突了。

### 方案二

* Liquibase 加上 compatibleMode=oracle 参数；
* flyway 单独配置数据源，不加 compatibleMode=oracle 参数。

```yaml
# 此处仅演示说明 datasource 相关配置，省略了 liquibase 和 flyway 的其他配置，完整的配置请自行补充。
spring:
  datasource:
    driver-class-name: dm.jdbc.driver.DmDriver
    url: jdbc:dm://localhost:5236?compatibleMode=oracle
    username: SYSDBA
    password: 123456
  liquibase:
    enabled: true
  flyway:
    enabled: true
    user: SYSDBA
    password: 123456
    driverClassName: dm.jdbc.driver.DmDriver
    url: jdbc:dm://localhost:5236
```

### 方案三

有了方案二中，单独配置 flyway 数据源的思路，那么当然也有单独配置 liquibase 的思路，以及同时单独配置 flyway 和 liquibase 数据源的思路。

总结一下就是有以下任意一项配置即可解决当前情景下的冲突：

* **单独配置 flyway 数据源**；
* **单独配置 liquibase 数据源**；
* **同时配置 flyway 和 liquibase 数据源**；

下面列出相关参考配置：

```yaml
spring:
  datasource:
    driver-class-name: dm.jdbc.driver.DmDriver
    url: jdbc:dm://localhost:5236
    username: SYSDBA
    password: 123456
  # 参考 org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties.java 类。
  liquibase:
    enabled: true
    user: SYSDBA
    password: 123456
    driverClassName: dm.jdbc.driver.DmDriver
    url: jdbc:dm://localhost:5236?compatibleMode=oracle
  # 参考 org.springframework.boot.autoconfigure.flyway.FlywayProperties.java 类。
  flyway:
    enabled: true
    user: SYSDBA
    password: 123456
    driverClassName: dm.jdbc.driver.DmDriver
    url: jdbc:dm://localhost:5236
```