# MySQL、Oracle、PostgreSQL 等数据库使用Flyway 的温馨提示
flyway-core 8.2.1及以后的版本不再直接支持 MySQL，需要额外引入：

Flyway MySQL 官方文档：[https://documentation.red-gate.com/fd/mysql-184127601.html](https://documentation.red-gate.com/fd/mysql-184127601.html)
```xml
<dependency>
    <artifactId>flyway-core</artifactId>
    <groupId>org.flywaydb</groupId>
</dependency>
<dependency>
  <groupId>org.flywaydb</groupId>
  <artifactId>flyway-mysql</artifactId>
</dependency>
```

flyway-core 9.22.3 及以后的版本不再直接支持 Oracle(Flyway 9.16.3 还是直接支持 Oracle 的)，需要额外引入：

Flyway Oracle 官方文档：[https://documentation.red-gate.com/flyway/flyway-cli-and-api/supported-databases/oracle-database](https://documentation.red-gate.com/flyway/flyway-cli-and-api/supported-databases/oracle-database)
```xml
<dependency>
    <artifactId>flyway-core</artifactId>
    <groupId>org.flywaydb</groupId>
</dependency>
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-database-oracle</artifactId>
</dependency>
```

其他官方支持的数据库类似，可参考官方文档（或者本仓库 demo 工程里已有的示例），这里不在赘述。
