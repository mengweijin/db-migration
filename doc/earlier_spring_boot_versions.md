## 在 spring boot 低版本中的使用

spring boot 低版本默认引入的 flyway 或 liquibase 可能不符合 db-migration-dm 的版本要求，是不是就不能使用了？

答案是：不一定。

### liquibase 

这里以 spring boot 2.3.2.RELEASE 版本为例。默认使用的是 liquibase 3.8.9 版本，不符合 db-migration-dm 的版本要求。

需要做以下调整：修改 liquibase 版本至：4.9.1。

```xml
<dependency>
    <groupId>org.liquibase</groupId>
    <artifactId>liquibase-core</artifactId>
    <version>4.9.1</version>
</dependency>
```

只需要明确指定 liquibase 版本号即可。

注：但不知道有没有隐藏问题，感觉是可以的，但还请多留心。

### flyway

这里以 spring boot 2.3.2.RELEASE 版本为例。默认使用的是 flyway 6.4.4 版本，不符合 db-migration-dm 的版本要求。

flyway 版本跨度太大，无法兼容。建议升级 spring boot 版本。

### 附：spring boot 官方支持终止时间

| Branch | Initial Release | End of Support | Enterprise Support |
|:-------|:----------------|:---------------|:-------------------|
| 3.3.x  | 2024-05-23      | 2025-05-23     | 2026-08-23         |
| 3.0.x  | 2022-11-24      | ~~2023-11-24~~ | 2025-02-24         |
| 2.7.x  | 2022-05-19      | ~~2023-11-24~~ | 2025-08-24         |
| 2.6.x  | 2021-11-17      | ~~2022-11-24~~ | ~~2024-02-24~~     |

可见，spring boot 2.7.x 版本，社区的支持早在 2023-11-24 就都已经终止了。

[https://spring.io/projects/spring-boot#support](https://spring.io/projects/spring-boot#support)

