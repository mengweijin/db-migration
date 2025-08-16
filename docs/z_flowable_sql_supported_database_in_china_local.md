# Flowable_脚本适配国产数据库及集成 Liquibase 或 Flyway 使用

建议自行通过修改 Flowable 脚本的方式得到 .sql 文件，然后通过 Liquibase 或 Flyway 去执行该脚本来创建 Flowable 表。

注：Flowable 官方 7.1.0 + 版本也已经移除了默认使用的 Liquibase。

## 达梦
达梦兼容 Oracle 可直接使用。参考：[达梦数据库使用 Liquibase 和 Flowable](./dm_use_liquibase_flowable.md)

## 其它

其它的建议自行直接从官方仓库获取要用的版本的所有脚本，然后自己修改适配数据库脚本。

当然，此种方式也适用于达梦。

以**人大金仓 GBase8s**数据库为例：

比如：Flowable 6.8.1 从 Flowable Github 里找：[flowable.oracle.all.create.sql](https://github.com/flowable/flowable-engine/blob/flowable-release-6.8.1/distro/sql/create/all/flowable.oracle.all.create.sql)

以此作为基础来修改（也可基于其它数据库脚本来修改）。

GBase8s 语句的修改可参考仓库 db-migration 下：【[6.8.1.flowable.gbase8s.all.create.change.md](../flowable/docs/6.8.1.flowable.gbase8s.all.create.change.md)】文档。

这里已经修改了一个 Flowable 6.8.1 版本的脚本：[6.8.1.flowable.gbase8s.all.create.sql](../flowable/6_8_1/6.8.1.flowable.gbase8s.all.create.sql)

**注意！！！**：这里修改的脚本是按照上面文档中修改的点来的，请自行排查是否有其它影响。不放心可自行修改脚本，避免直接使用。

sql 脚本修改完成后，可直接用 flyway 或 Liquibase 去自动执行。这样同样可以自动化，免去手动执行的烦恼。

### Liquibase

以 GBase8s 为例：

现在，假设你已经得到了一个修改后的 Flowable 6.8.1 版本的创建表的所有脚本：[6.8.1.flowable.gbase8s.all.create.sql](../flowable/6_8_1/6.8.1.flowable.gbase8s.all.create.sql)

#### maven

```xml
<!--注意引入顺序，db-migration 必须在前面先引入。-->
<dependency>
    <groupId>com.github.mengweijin</groupId>
    <artifactId>db-migration</artifactId>
    <version>${db-migration.version}</version>
</dependency>
<!--liquibase 的版本固定使用 4.27.0 -->
<dependency>
    <groupId>org.liquibase</groupId>
    <artifactId>liquibase-core</artifactId>
    <version>4.27.0</version>
</dependency>
<dependency>
    <groupId>com.gbasedbt</groupId>
    <artifactId>jdbc</artifactId>
    <version>3.5.1</version>
    <!-- 3.6.3.2 版本驱动包启动有问题 -->
    <!--<version>3.6.3.2</version>-->
</dependency>
<dependency>
    <groupId>org.flowable</groupId>
    <artifactId>flowable-spring-boot-starter-process</artifactId>
    <version>${flowable.version}</version>
</dependency>
```

#### spring boot 配置

注意以下几点：

- spring.liquibase.change-log:classpath:/db/changelog.xml
  - 中配置的 xml 文件中写入你修改后的 .sql 脚本，让应用启动时 liquibase 可以执行你修改的脚本。
- flowable.database-schema-update: false
  - 要关闭 flowable 默认启动时的更新表操作。把它设置为 false。

详细可参考示例工程：[gbase8s-liquibase-flowable](../demo-gbase8s/gbase8s-liquibase-flowable)

```yaml
spring:
  profiles:
    active: local
  datasource:
    driver-class-name: com.gbasedbt.jdbc.Driver
    url: jdbc:gbasedbt-sqli://localhost:9088/testdb:GBASEDBTSERVER=gbase01
    username: gbasedbt
    password:
  liquibase:
    # 启用liquibase
    enabled: true
    # 存储变化的文件（changelog）位置。默认：classpath:/db/changelog/db.changelog-master.xml
    change-log: classpath:/db/changelog.xml
    # 分环境执行，若在 changelog 文件中设置了对应 context 属性，则只会执行与 dev 对应值的 changeset
    # contexts: dev
    # 执行前首先删除数据库，默认 false。若设置为 true，则执行变更前，会先删除目标数据库，请谨慎
    # dropFirst: false
    # 执行更新时将回滚 SQL 写入的文件路径
    # rollback-file:
    # 如果使用工程已配置的 datasource 数据源，则以下三个数据库连接参数可不配置
    # 访问数据库的连接地址
    # url:
    # 访问数据库的用户名
    # user:
    # 访问数据库的密码
    # password:
flowable:
  database-schema-update: false
```

### Flyway

Flyway 的使用方式和 Liquibase 类似，依然是：

假设你已经得到了一个修改后的 Flowable 6.8.1 版本的创建表的所有脚本：[6.8.1.flowable.gbase8s.all.create.sql](../flowable/6_8_1/6.8.1.flowable.gbase8s.all.create.sql)

然后 maven 引入 db-migration、flyway、flowable 等。

#### spring boot 配置

注意以下几点：

- spring.flyway.locations:classpath:db/migration/gbase
    - 中把脚本 **6.8.1.flowable.gbase8s.all.create.sql** 按照 Flyway 的规则命名，并且放到指定位置。
    - 这里配置的位置是：类路径下的 resources/db/migration/gbase/6.8.1.flowable.gbase8s.all.create.sql
- spring.liquibase.enabled: false
  - 使用了 flyway，就可以把 flowable 自带的 liquibase 禁用掉，或者直接从 maven 中排除掉。
- flowable.database-schema-update: false
    - 要关闭 flowable 默认启动时的更新表操作。把它设置为 false。

```yaml
spring:
  profiles:
    active: local
  datasource:
    driver-class-name: com.gbasedbt.jdbc.Driver
    url: jdbc:gbasedbt-sqli://localhost:9088/testdb:GBASEDBTSERVER=gbase01
    username: gbasedbt
    password:
  # 禁用掉 liquibase
  liquibase:
    enabled: false
  flyway:
    # 默认不启用，true 为启用
    enabled: true
    baseline-on-migrate: true
    # baseline-version：产生的原因是兼容已经有版本发布的项目（即数据库中原本就存在一些表），要满足 3 个条件：
    # 1. baseline-on-migrate: true
    # 2. 数据库中已经存在其他表。
    # 3. flyway_schema_history 表不存在。
    # 当以上 3 个条件成立时，设置的 baseline-version 的值是多少，那么这个版本及之前版本的脚本都不会被执行。
    # 并且，flyway_schema_history 表中会多出第一条字段 script 为 << Flyway Baseline >> 的数据记录。
    # 不需要 baseline-version 的话可以注释掉。需要的话比如配置为：baseline-version: 2020.12.11
    baseline-version: 
    # 禁用 placeholder replacement，否则 sql 脚本中不能写 ${} 这样的字符。
    placeholder-replacement: false
    # flyway脚本命名规则为：V<VERSION>__<NAME>.sql (with <VERSION> an underscore-separated version, such as ‘1’ or ‘2_1’)
    # flyway在spring boot中默认配置位置为：classpath:db/migration
    locations:
      - classpath:db/migration/gbase
      # - classpath:db/migration/h2
      # - classpath:db/migration/mysql
      # - classpath:db/migration/oracle
flowable:
  database-schema-update: false
```

## 附录
Flowable 升级脚本：[https://github.com/flowable/flowable-engine/tree/main/distro/sql/upgrade/all](https://github.com/flowable/flowable-engine/tree/main/distro/sql/upgrade/all)
