达梦兼容 Oracle 可直接使用，其它的建议自行直接从官方仓库获取要用的版本的所有脚本，然后自己修改适配数据库脚本。

以 GBase8s 数据库为例：

比如：6.8.0 从这里找：[https://github.com/flowable/flowable-engine/tree/flowable-release-6.8.0/distro/sql/create/all](https://github.com/flowable/flowable-engine/tree/flowable-release-6.8.0/distro/sql/create/all)

语句的修改参考仓库 db-migration 下：【db-migration/doc/z_flowable_适配_GBase_8s.md】 中的【修改sql语句】章节。

修改完成后，直接用 flyway 或 Liquibase 去自动执行。

这样同样可以自动化，免去手动执行的烦恼。

升级脚本：

[https://github.com/flowable/flowable-engine/tree/main/distro/sql/upgrade/all](https://github.com/flowable/flowable-engine/tree/main/distro/sql/upgrade/all)