## liquibase 4.9.1

spring boot 2.7.x 默认使用 liquibase 4.9.1 版本，随着 db-migration-dm 对 liquibase 新版本的更新适配，跨度已经太大了，很难在同一包里兼容所有版本。

此时，可以尝试升级 liquibase 版本。比如：升级 liquibase 版本至 db-migration-dm 最新版当前支持的 liquibase  4.17.2 版本。

这里以 spring boot 2.7.18 版本为例。默认使用的是 liquibase 4.9.1 版本，不符合 db-migration-dm **最新版本**的要求。

需要做以下调整：修改 liquibase 版本至：4.17.2。

```xml
<dependency>
    <groupId>org.liquibase</groupId>
    <artifactId>liquibase-core</artifactId>
    <version>4.17.2</version>
</dependency>
```

只需要明确指定 liquibase 版本号即可。

注：不知道还有没有隐藏问题，感觉是可以的，请多留心。


## liquibase 3.8.9

这里以 spring boot 2.3.2.RELEASE 版本为例。默认使用的是 liquibase 3.8.9 版本，不符合 db-migration-dm 的版本要求。

需要做以下调整：匹配并修改 db-migration-dm 和 liquibase 的版本。

如果情况一使用起来能满足当下要求的话，建议优先使用情况一的方式，不行的话可以尝试使用情况二的方式。

### 情况一：使用 db-migration-dm 最新版本

```xml
<dependency>
    <groupId>com.github.mengweijin</groupId>
    <artifactId>db-migration-dm</artifactId>
    <version>${db-migration-dm.version}</version>
</dependency>
<dependency>
    <groupId>org.liquibase</groupId>
    <artifactId>liquibase-core</artifactId>
    <version>4.17.2</version>
</dependency>
```

### 情况二：使用 db-migration-dm 1.1.8 版本来支持 liquibase 4.9.1 版本。

```xml
<dependency>
    <groupId>com.github.mengweijin</groupId>
    <artifactId>db-migration-dm</artifactId>
    <version>1.1.8</version>
</dependency>
<dependency>
    <groupId>org.liquibase</groupId>
    <artifactId>liquibase-core</artifactId>
    <version>4.9.1</version>
</dependency>
```

注：不知道还有没有隐藏问题，感觉是可以的，请多留心。
