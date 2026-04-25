# 在 Spring Boot 4 中使用 db-migration

- 版本参考（需要 db-migration >= 3.0.0）：`README.md` 中的`db-migration 版本说明`表格。
- Maven 坐标参考：`README.md` 中的 `Maven 坐标`章节。

## 使用文档

### 示例一：达梦数据库 + Flyway + Spring Boot

```xml
<project>
    <properties>
        <!-- 锁定 flyway 版本号 -->
        <flyway.version>12.1.1</flyway.version>
        <!-- db-migration 版本 -->
        <db-migration.version>3.0.0</db-migration.version>
    </properties>
    <dependencies>
        <!-- 引入要使用的 maven 坐标（其他数据库可引入对应的坐标） -->
        <dependency>
            <groupId>com.github.mengweijin</groupId>
            <artifactId>db-migration-dameng-flyway</artifactId>
            <version>${db-migration.version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-flyway</artifactId>
            <exclusions>
                <!-- 排除掉 spring-boot-flyway 中的 flyway-core 包 -->
                <exclusion>
                    <groupId>org.flywaydb</groupId>
                    <artifactId>flyway-core</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
    </dependencies>
</project>
```

> 注意：
> 1. 使用 `flyway.version` 锁定 flyway 版本号（参考 `db-migration 版本说明` 章节）。
> 2. 引入要使用的 maven 坐标（其他数据库可引入对应的坐标）。
> 3. JDBC Driver 驱动包请自行引入。
> 4. Maven 需要排除掉 spring-boot-flyway 中的 flyway-core 包。

然后按照 Flyway 原来的的使用方式使用即可。

其他`数据库 + Flyway` 的使用方式类似，不再赘述。


### 示例二：达梦数据库 + Liquibase + Spring Boot

```xml
<project>
    <properties>
        <!-- 锁定 liquibase 版本号 -->
        <liquibase.version>4.31.1</liquibase.version>
        <!-- db-migration 版本 -->
        <db-migration.version>3.0.0</db-migration.version>
    </properties>
    <dependencies>
        <!-- 引入要使用的 maven 坐标（其他数据库可引入对应的坐标） -->
        <dependency>
            <groupId>com.github.mengweijin</groupId>
            <artifactId>db-migration-dameng-liquibase</artifactId>
            <version>${db-migration.version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-liquibase</artifactId>
            <exclusions>
                <!-- 排除掉 spring-boot-liquibase 中的 liquibase-core 包 -->
                <exclusion>
                    <groupId>org.liquibase</groupId>
                    <artifactId>liquibase-core</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
    </dependencies>
</project>
```

> 注意：
> 1. 使用 `liquibase.version` 锁定 liquibase 版本号（参考 `db-migration 版本说明` 章节）。
> 2. 引入要使用的 maven 坐标（其他数据库可引入对应的坐标）。
> 3. JDBC Driver 驱动包请自行引入。
> 4. Maven 需要排除掉 spring-boot-liquibase 中的 liquibase-core 包。
> 5. 若数据库用户名和 schema 不一致时，需要配置 spring.liquibase.default-schema=<testdb>

然后按照 Liquibase 原来的的使用方式使用即可。

其他`数据库 + Liquibase` 的使用方式类似，不再赘述。

## 示例工程

- 代码分支 `master` 下的：
  - `demo-dameng`
  - `demo-gbase8s`
  - `demo-opengauss`
