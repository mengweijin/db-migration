# 在 Spring Boot 2 和 spring boot 3 中使用 db-migration

- 版本参考（需要 db-migration < 3.0.0）：`README.md` 中的`db-migration 版本说明`表格。
- Maven 坐标参考：`README.md` 中的 `Maven 坐标`章节。

## 使用文档

### 示例一：达梦数据库 + Flyway

```xml
<project>
    <properties>
        <!-- 锁定 flyway 版本号，低版本比如 spring boot 2.4.* 可对应修改为：7.15.0 -->
        <flyway.version>10.10.0</flyway.version>
        <!-- db-migration 版本 -->
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
> 4. Maven 无需单独引入 `flyway-core` 包，如有的话，需要排除掉。

然后按照 Flyway 原来的的使用方式使用即可。

其他`数据库 + Flyway` 的使用方式类似，不再赘述。


### 示例二：达梦数据库 + Liquibase

```xml
<project>
    <properties>
        <!-- 锁定 liquibase 版本号 -->
        <liquibase.version>4.27.0</liquibase.version>
        <!-- db-migration 版本 -->
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
> 4. Maven 无需单独引入 `liquibase-core` 包，如有的话，需要排除掉。

然后按照 Liquibase 原来的的使用方式使用即可。

其他`数据库 + Liquibase` 的使用方式类似，不再赘述。

## 示例工程

代码分支 `2.2.x` 下的：`demo-dameng` 文件夹。

其他数据库的示例工程是旧的版本，还未及时更新。可参考达梦数据库示例工程 `demo-dameng`。 
