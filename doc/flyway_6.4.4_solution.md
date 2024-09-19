## flyway 低版本（不支持）

这里以 spring boot 2.3.2.RELEASE 版本为例。默认使用的是 flyway 6.4.4 版本，不符合 db-migration-dm 的版本要求。

flyway 版本跨度太大，无法兼容。**建议升级 spring boot 版本**，比如至少升级到 spring boot 2.6.x 版本或以上。

## 附：spring boot 官方支持终止时间

| Branch | Initial Release | End of Support | Enterprise Support |
|:-------|:----------------|:---------------|:-------------------|
| 3.3.x  | 2024-05-23      | 2025-05-23     | 2026-08-23         |
| 3.0.x  | 2022-11-24      | ~~2023-11-24~~ | 2025-02-24         |
| 2.7.x  | 2022-05-19      | ~~2023-11-24~~ | 2025-08-24         |
| 2.6.x  | 2021-11-17      | ~~2022-11-24~~ | ~~2024-02-24~~     |

可见，spring boot 2.7.x 版本，社区的支持早在 2023-11-24 就都已经终止了。

[https://spring.io/projects/spring-boot#support](https://spring.io/projects/spring-boot#support)

